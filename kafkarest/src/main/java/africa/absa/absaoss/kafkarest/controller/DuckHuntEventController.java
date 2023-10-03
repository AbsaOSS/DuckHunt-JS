package africa.absa.absaoss.kafkarest.controller;

import africa.absa.absaoss.kafkarest.model.DuckHuntEvent;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.SerializationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


@RestController
class DuckHuntEventController {
    Logger logger = Logger.getLogger(DuckHuntEvent.class.getName());
    @PostMapping("/rest/postEvent")
    Boolean postEvent(@RequestBody DuckHuntEvent duckHuntEvent) {
        logger.log(Level.INFO, duckHuntEvent.toString());
        KafkaProducer<String, DuckHuntEvent> producer = getProducer();
        String key = duckHuntEvent.getEmail().toString();

        ProducerRecord<String, DuckHuntEvent> record = new ProducerRecord<>("duck_hunt_demo", key, duckHuntEvent);
        try {
            producer.send(record, (metadata, exception) -> {
                if (exception != null) logger.log(Level.SEVERE, "Callback", exception);
                else logger.log(Level.INFO, "Callback: " + metadata.offset());
            });
        } catch (SerializationException exception) {
            // may need to do something with it
            logger.log(Level.SEVERE, "Sending", exception);
        }
        producer.flush();
        return true;
    }

    private KafkaProducer<String, DuckHuntEvent> producer = null;
    private KafkaProducer<String, DuckHuntEvent> getProducer() {
        if (producer != null) return producer;
        synchronized (this) {
            if (producer == null) {
                Properties props = new Properties();
                props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "pkc-q283m.af-south-1.aws.confluent.cloud:9092");
                props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                        org.apache.kafka.common.serialization.StringSerializer.class);
                props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                        io.confluent.kafka.serializers.KafkaAvroSerializer.class);
                props.put("auto.register.schemas", "false");
                props.put("security.protocol", "SASL_SSL");
                // B3FBQ5SJB4UN7HG4
                // v2A7kQBIU966N1Tl/S5tdZuOrvICNX7p8W/miiv/pNgIfJ/2iCv4CZh6+n0jU0uM
                props.put("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"LLQSHPFJ6MEFR2KI\" password=\"23EFuA6WhxWuxrzMQoSJ9FreGUJ6reW+71yi8XMRAM2Rip39NkYS1/FMeWnGl1U+\";");
                props.put("sasl.mechanism", "PLAIN");
                props.put("client.dns.lookup", "use_all_dns_ips");
                props.put("schema.registry.url", "https://psrc-4v1qj.eu-central-1.aws.confluent.cloud");
                props.put("basic.auth.credentials.source", "USER_INFO");
                props.put("basic.auth.user.info", "B3FBQ5SJB4UN7HG4:v2A7kQBIU966N1Tl/S5tdZuOrvICNX7p8W/miiv/pNgIfJ/2iCv4CZh6+n0jU0uM");
                producer = new KafkaProducer<>(props);
            }
            return producer;
        }
    }

}
