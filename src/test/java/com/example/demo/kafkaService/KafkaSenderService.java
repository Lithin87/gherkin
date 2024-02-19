package com.example.demo.kafkaService;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.KafkaException;
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

     
    public void sendMessage(String topic, int partition, String key, String message) {
        try {

            ProducerRecord<String, String> record = new ProducerRecord<>(topic, partition , key , message);
            CompletableFuture<SendResult<String, String>> result =   kafkaTemplate.send(record);
            result.join();
            if(result!= null) {
                SendResult<String, String> sendResult = result.get();
                RecordMetadata metadata = sendResult.getRecordMetadata();
                String successMessage = "\n Message write to topic: " + topic + " partition: " + metadata.partition() + " offset:" + metadata.offset();
                System.out.println(successMessage);
            }
        } catch ( InterruptedException | ExecutionException | KafkaException exception) {
            System.out.println("EXCEPTION"+ exception);
          }

    }
}
 