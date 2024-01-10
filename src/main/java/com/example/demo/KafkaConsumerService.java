package com.example.demo;


import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

@Component
public class KafkaConsumerService {
    @Value("${bootstrap-servers}")
    private String bootstrapServers;
    private String verifyMessage;
    private List<String> receivedMessages = new ArrayList<>();

    @KafkaListener(topics = "input", groupId = "myGroup")
    public void consumeMessage(String message) throws InterruptedException {
        setVerifyMessage(message);
        System.out.println("Received Message: " + message);
    }



    public void setVerifyMessage(String verifyMessage) {
        this.verifyMessage = verifyMessage;

    }

    public String getVerifyMessage() {
        return verifyMessage;
    }

    public void subscribeConsumerToTopic(String topic) {
        Properties consumerProps = new Properties();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "test-consumer-group");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        try (Consumer<String, String> consumer = new KafkaConsumer<>(consumerProps)) {
            // Subscribe to the Kafka topic
            consumer.subscribe(Collections.singletonList(topic));

            System.out.println("Consumer subscribed to Kafka topic: " + topic);

            // Poll for messages (you can customize this based on your test logic)
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(5));

            // Process the received messages if needed
            records.forEach(record -> System.out.println("Received message: " + record.value()));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean isMessageReceived(String expectedMessage) {
        return receivedMessages.contains(expectedMessage);
    }
}
 