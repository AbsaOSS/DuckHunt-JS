package africa.absa.ursa.minor.lambda.handler;

import africa.absa.ursa.minor.lambda.model.DuckHuntEvent;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;

public class Handler implements RequestHandler<Map<String,Object>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public String handleRequest(Map<String, Object> event, Context context) {
        var logger = context.getLogger();
        String body = (String) event.get("body");
        if (body == null) {
            logger.log("No Body");
            return "Failed - No Body";
        }
        try {
            DuckHuntEvent duckHuntEvent = objectMapper.readValue(body, DuckHuntEvent.class);
            logger.log(duckHuntEvent.toString());
            KafkaProducer<String, DuckHuntEvent> producer = getProducer(logger);
            String key = duckHuntEvent.getEmail().toString();

            ProducerRecord<String, DuckHuntEvent> record = new ProducerRecord<>("duck_hunt_demo", key, duckHuntEvent);
            try {
                logger.log("Sending");
                //THis fails
                producer.send(record, (metadata, exception) -> {
                    if (exception != null) {
                        logger.log("Callback exception : " + exception.getMessage());
                        logger.log(convertStackTrace(exception));
                    }
                    else logger.log("Callback: " + metadata.offset());
                });
                // This works
                //postMessage(duckHuntEvent.getEmail().toString(), duckHuntEvent.getEventType().toString(), duckHuntEvent.getEventSize(), logger);
            } catch (Exception exception) {
                // may need to do something with it
                logger.log("Sending exception" + exception.getMessage());
                logger.log(convertStackTrace(exception));
            }
            producer.flush();
        } catch (JsonProcessingException e) {
            logger.log("Body not a duckhunt");
            logger.log(e.getMessage());
            return "Failed - Body not a duckhunt";
        }
        return "Success";
    }
    private static String convertStackTrace(Throwable throwable) {
        try (StringWriter sw = new StringWriter();
             PrintWriter pw = new PrintWriter(sw)) {

            throwable.printStackTrace(pw);

            return sw.toString();

        } catch (IOException ioe) {
            // can never really happen..... convert to unchecked exception
            throw new IllegalStateException(ioe);
        }
    }
    private KafkaProducer<String, DuckHuntEvent> producer = null;
    private KafkaProducer<String, DuckHuntEvent> getProducer(LambdaLogger logger) {
        if (producer != null) return producer;
        synchronized (this) {
            if (producer == null) {
                logger.log("Creating new Producer");
                Properties props = new Properties();
                props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "pkc-q283m.af-south-1.aws.confluent.cloud:9092");
                props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                        org.apache.kafka.common.serialization.StringSerializer.class);
                props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                        io.confluent.kafka.serializers.KafkaAvroSerializer.class);
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


    private static void postMessage(String email, String eventType, int eventSize, LambdaLogger logger) throws IOException {
        HttpURLConnection con = getHttpURLConnection();
        String jsonInputString = "{\n" +
                "    \"key\": {\n" +
                "        \"type\": \"STRING\",\n" +
                "        \"data\": \"" + email + "\"\n" +
                "    },\n" +
                "    \"value\": {\n" +
                "        \"type\": \"JSON\",\n" +
                "        \"data\": {\n" +
                "            \"email\": \"" + email + "\",\n" +
                "            \"eventType\": \"" + eventType + "\",\n" +
                "            \"eventSize\": " + eventSize + "\n" +
                "        }\n" +
                "    }\n" +
                "}";
        try(OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            logger.log("Response Code: " + con.getResponseCode());
            logger.log("Response Message: " + con.getResponseMessage());
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            logger.log(response.toString());
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
}
