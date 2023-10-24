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
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

public class Handler implements RequestHandler<Map<String,Object>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String handleRequest(Map<String, Object> event, Context context) {
        var logger = context.getLogger();
        event.forEach((k, v) -> {
            logger.log(k + " : " + v.toString());
        });
        String body = (String) event.get("body");
        if (body == null) {
            String rawPath = (String) event.get("rawPath");
            if (rawPath == null || rawPath.isEmpty()) {
                logger.log("No Body or Path");
                return "Failed - No Body or Path";
            } else {
                switch(rawPath) {
                    case "/ksql" : {
                        Map<String, Object> queryStringParameters = (Map<String, Object>) event.get("queryStringParameters");
                        queryStringParameters.forEach((k, v) -> {
                            logger.log(k + " : " + v.toString());
                        });
                        String query = (String) queryStringParameters.get("query");
                        if (!query.endsWith(";")) query += ";";
                        if (query == null || query.isEmpty()) return "No KSQL Query";
                        try {
                            return postKSQLQuery(query);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    default: return "unsupported : " + rawPath;
                }
            }
        } else {
            AtomicReference<String> result = new AtomicReference<>("Not Done");
            try {
                DuckHuntEvent duckHuntEvent = objectMapper.readValue(body, DuckHuntEvent.class);
                logger.log(duckHuntEvent.toString());
                KafkaProducer<String, String> producer = getProducer(logger);
                String key = duckHuntEvent.getEmail().toString();

                ProducerRecord<String, String> record = new ProducerRecord<>("duck_hunt_demo", key, duckHuntEvent.toString());
                try {
                    logger.log("Sending");
                    producer.send(record, (metadata, exception) -> {
                        if (exception != null) {
                            logger.log("Callback exception : " + exception.getMessage());
                            logger.log(convertStackTrace(exception));
                            result.set("Failed");
                        } else {
                            logger.log("Callback: " + metadata.offset());
                            result.set("Success");
                        }
                    });
                    while (result.get().equals("Not Done")) {
                        Thread.sleep(10);
                    }
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
            return result.get();
        }
    }

    private String postKSQLQuery(String query) throws IOException {
        URL url = new URL("https://pksqlc-71x0j.af-south-1.aws.confluent.cloud/query");
        HttpURLConnection con = getHttpURLConnection(query, url);
        boolean firstLine = true;
        String delimiter = "";
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                if (firstLine) {
                    response.append("{\"query\": ");
                    response.append(responseLine.trim());
                    response.append(", \"results\": [");
                    firstLine = false;
                } else {
                    response.append(delimiter);
                    response.append(responseLine.trim());
                    delimiter = ", ";
                }

            }
            response.append("]}");
            return response.toString();
        }
    }

    private static HttpURLConnection getHttpURLConnection(String query, URL url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        String basicAuth = "Basic S0VUS09PV01ZRUw2WVA1STo4Wkh0bnVUdkwwc3hWRTl4WmVRRnpBTHRhTDM4QmgzSk1xbS8xRzliTWxpWFJTNGcrc1lUaXZiY1I1THRUS21j";
        con.setRequestProperty("Authorization", basicAuth);
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        String requestJson = String.format("""
{
  "ksql": "%s",
  "streamsProperties": {
      "auto.offset.reset" : "earliest"
  }
}
""", query);
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = requestJson.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        return con;
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
    private KafkaProducer<String, String> producer = null;
    private KafkaProducer<String, String> getProducer(LambdaLogger logger) {
        if (producer != null) return producer;
        synchronized (this) {
            if (producer == null) {
                logger.log("Creating new Producer");
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
}
