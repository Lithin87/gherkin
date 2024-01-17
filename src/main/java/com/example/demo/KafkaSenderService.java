package com.example.demo;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

@Service
public class KafkaSenderService {

    @Autowired
    private final KafkaTemplate<String, String> kafkaTemplate = null;


    public void sendMessage(String topic, String message) {
       CompletableFuture<SendResult<String, String>> completableFuture =  kafkaTemplate.send(topic, message);
       completableFuture.join();
       System.out.println("Message sent successfully to Kafka topic: " + topic);
    }
}
 