package africa.absa.ursa.minor;

import africa.absa.absaoss.kafkarest.model.DuckHuntEvent;
import africa.absa.absaoss.kafkarest.model.EventType;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedQueue;

import static java.lang.System.out;

/**
 * Hello world!
 *
 */
public class App implements Runnable
{
    public App(String email) {
        this.email = email;
    }

    private static ConcurrentLinkedQueue<DuckHuntEvent> queue = new ConcurrentLinkedQueue<>();
    private static class QueueHandler implements Runnable {

        @Override
        public void run() {
            int sent = 0;
            out.println("Queue Handler");
            while(running || !queue.isEmpty()) {
                if (queue.isEmpty()) {
                    try {
                        out.println("Queue empty");
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    List<DuckHuntEvent> duckHuntEvents = new ArrayList<>();
                    while(!queue.isEmpty() && duckHuntEvents.size() < 100) {
                        duckHuntEvents.add(queue.remove());
                    }
                    out.println("Sending: " + duckHuntEvents.size());
                    try {
                        postMessage(duckHuntEvents);
                        sent+= duckHuntEvents.size();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            out.println("QueueHandler done: " + sent);
        }
    }

    private static boolean running = true;
    public static void main(String[] args ) throws IOException, InterruptedException {
        running = true;
        //new Thread(new QueueHandler()).start();
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(new App("email" + i + "@absa.africa"));
            thread.start();
            threads.add(thread);
        }
        while(running) {
            boolean stillRunning = false;
            for (Thread thread :
                    threads) {
                stillRunning |= threads.get(0).isAlive();
            }
            running = stillRunning;
            Thread.sleep(100);
        }
    }

    private static void postMessage(List<DuckHuntEvent> duckHuntEvents) throws IOException {
        HttpURLConnection con = getHttpURLConnection();
        StringBuilder requestJson = new StringBuilder();
        duckHuntEvents.forEach((duckHuntEvent -> {
            requestJson.append("{\n" +
                    "    \"key\": {\n" +
                    "        \"type\": \"STRING\",\n" +
                    "        \"data\": \"" + duckHuntEvent.getEmail() + "\"\n" +
                    "    },\n" +
                    "    \"value\": {\n" +
                    "        \"type\": \"JSON\",\n" +
                    "        \"data\": {\n" +
                    "            \"email\": \"" + duckHuntEvent.getEmail() + "\",\n" +
                    "            \"eventType\": \"" + duckHuntEvent.getEventType().toString() + "\",\n" +
                    "            \"eventSize\": " + duckHuntEvent.getEventSize() + "\n" +
                    "        }\n" +
                    "    }\n" +
                    "}\n");
        }));
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = requestJson.toString().getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }
    }

    private static HttpURLConnection getHttpURLConnection() throws IOException {
        URL url = new URL("https://pkc-q283m.af-south-1.aws.confluent.cloud/kafka/v3/clusters/lkc-zmowrd/topics/duck_hunt_demo/records");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Basic TExRU0hQRko2TUVGUjJLSToyM0VGdUE2V2h4V3V4cnpNUW9TSjlGcmVHVUo2cmVXKzcxeWk4WE1SQU0yUmlwMzlOa1lTMS9GTWVXbkdsMVUr");
        con.setDoOutput(true);
        return con;
    }

    private static void writeTopic(DuckHuntEvent duckHuntEvent) {

        KafkaProducer<String, String> producer = getProducer();
        String key = duckHuntEvent.getEmail().toString();

        ProducerRecord<String, String> record = new ProducerRecord<>("duck_hunt_demo", key, duckHuntEvent.toString());
        out.println("send");
        producer.send(record, (metadata, exception) -> {
            out.println("Callback");
            if (exception != null) exception.printStackTrace();
        });
        producer.flush();
    }

    private static final String lock = "Lock";
    private static KafkaProducer<String, String> producer = null;
    private static KafkaProducer<String, String> getProducer() {
        if (producer != null) return producer;
        synchronized (lock) {
            if (producer == null) {
                try{
                    InetAddress address = InetAddress.getByName("pkc-q283m.af-south-1.aws.confluent.cloud");
                    System.out.println(address);
                }
                catch(UnknownHostException ex){
                    System.out.println("pkc-q283m.af-south-1.aws.confluent.cloud");
                }
                Properties props = new Properties();
                props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "pkc-q283m.af-south-1.aws.confluent.cloud:9092");
                props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                        org.apache.kafka.common.serialization.StringSerializer.class);
                props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                        org.apache.kafka.common.serialization.StringSerializer.class);
                props.put("auto.register.schemas", "false");
                props.put("security.protocol", "SASL_SSL");
                props.put("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule required username='PVO6LL6OSHMJETKW' password='23K3i/WwtrO0q3UHly0565qSwmFmf9bvEbqrEhRHQjRdD/0ZLRa1iqoTaocDDWwF';");
                props.put("sasl.mechanism", "PLAIN");
                props.put("client.dns.lookup", "use_all_dns_ips");
                props.put("client.id", "AWS_PRODUCER_DH_DEMO_1");
                props.put("schema.registry.url", "https://psrc-4v1qj.eu-central-1.aws.confluent.cloud");
                props.put("basic.auth.credentials.source", "USER_INFO");
                props.put("basic.auth.user.info", "4TZ2FCCTWURGIAZC:9W2GwQVIhm4RIQoBRAPZn2PQU8XkoZL9BvvG0q8tREZ/9X4ERMonqObkW7m2Plw8");
                producer = new KafkaProducer<>(props);
            }
            return producer;
        }
    }


    private String email;
    @Override
    public void run() {
        int score = 0;
        int messages = 0;
        out.println("Started: " + email);
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(Math.round(Math.random() * 1000));
                Double rand = Math.random();
                int hit = (rand > 0.9) ? 2 : rand > 0.5 ? 1 : 0;
                DuckHuntEvent value = DuckHuntEvent.newBuilder()
                        .setEmail(email)
                        .setEventSize(1)
                        .setEventType(EventType.SHOT)
                        .build();
                writeTopic(value);
                value = DuckHuntEvent.newBuilder()
                        .setEmail(email)
                        .setEventSize(hit)
                        .setEventType(EventType.HIT)
                        .build();
                writeTopic(value);
                score += 100 * hit;
                value = DuckHuntEvent.newBuilder()
                        .setEmail(email)
                        .setEventSize(score)
                        .setEventType(EventType.SCORE)
                        .build();
                writeTopic(value);
                messages += 3;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        out.println(email + ": " + messages);
    }
}
