package com.example.demo;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.stream.StreamSupport;

@Component
public class KafkaMessageVerifier {

    @Value("${bootstrap-servers}")
    private String bootstrapServers;

    boolean f;
    
    public boolean isF() {
        return this.f;
    }


    public void setF(boolean f) {
        this.f = f;
    }



    public boolean verifyMessage(String topic, String expectedMessage) {
        Properties consumerProps = new Properties();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "test-consumer-group");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        try (Consumer<String, String> consumer = new KafkaConsumer<>(consumerProps)) {
            consumer.subscribe(Collections.singletonList(topic));

            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(5)); // Adjust timeout as needed

            if (records.count() > 0) {
                if (StreamSupport.stream(records.spliterator(), false)
                        .anyMatch(record -> record.value().equals(expectedMessage))){
                   setF(true);
                };
            }else setF(false);
            return f ;
       } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
       
    }
}