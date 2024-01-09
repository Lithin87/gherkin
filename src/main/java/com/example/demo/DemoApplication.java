package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String topic = "your-topic"; // Replace with your actual topic
        String message = "Hello, Kafka!"; // Replace with your actual message

        CompletableFuture<CompletableFuture<SendResult<String,String>>> future = 
                CompletableFuture.supplyAsync(() -> kafkaTemplate.send(topic, message));

        future.thenAccept(result -> {
            System.out.println("Message sent successfully: " + result.toString());
        }).exceptionally(ex -> {
            System.err.println("Error sending message: " + ex.getMessage());
            return null;
        });

        // Ensure the main thread doesn't exit immediately, allowing time for the CompletableFuture to complete
        future.join();
    }
}