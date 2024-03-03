package com.example.app.service.impl;

import java.time.Duration;
import java.util.Set;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.app.service.ValidationTemplateInterface;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("kafka")
public class KafkaValidationImpl extends ValidationTemplateInterface {

    @Autowired
    private final KafkaTemplate<String, String> kafkaTemplate = null;

    @Autowired
    private KafkaConsumer<String, String> kafkaConsumer;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    protected void messageSend(String inputTopic,String outputTopic,  String jsonContent) {
        
        kafkaConsumer.subscribe(Set.of(outputTopic));
        kafkaConsumer.poll(Duration.ofMillis(2000));
        
        System.out.println("\n KAFKA sending ");
        kafkaTemplate.send(inputTopic, jsonContent);
    }


    @Override
    protected JsonNode messageListen(String outputTopic) {
        JsonNode root = null;
        try {

            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(100));

            for (ConsumerRecord<String, String> record : records) {

                try {
                    root = objectMapper.readTree(record.value());
                } catch (JsonProcessingException e) {
                    System.out.println("Parsing Error in Listen");
                }
            }
        } catch (Exception e) {
            System.out.println("\n Error occurred in kafka listening" + e.getMessage());
        }

        System.out.println("\n KAFKA messageListen :  ");
        return root;
    }


    @Override
    public boolean messageVerify(JsonNode ProcessedOutput, JsonNode ProvidedOutput) {
        try {
                return ProcessedOutput.equals(ProvidedOutput);
        } catch (Exception e) {
            System.out.println("\n Error occurred in kafka verifying" + e.getMessage());
            return false;
        }
    }


    @Override
    protected void simulate(String outputTopic, String ProvidedOutput) {
        System.out.println("\n KAFKA simulate");
        kafkaTemplate.send(outputTopic, ProvidedOutput);
    }

}