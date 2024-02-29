package com.example.app.service.impl;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
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
    private ConsumerFactory<String, String> consumerFactory;



    @Autowired
    ObjectMapper objectMapper;

    private JsonNode processed = null;

    @Override
    @Async
    protected void messageSend(String inputTopic, String jsonContent) {
        kafkaTemplate.send(inputTopic, jsonContent);
                System.out.println("\n Came in kafka sending ");
    }


    @Override
    @Async
    protected JsonNode messageListen(String outputTopic) {
        JsonNode root = null;
        try {

      KafkaConsumer<String, String> consumer = (KafkaConsumer<String, String>) consumerFactory.createConsumer();

      consumer.subscribe(Set.of(outputTopic));

      consumer.poll(Duration.ofMillis(0)); 
        for (TopicPartition partition : consumer.assignment()) {
            consumer.seekToEnd(Collections.singleton(partition));
        }

        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(6000));
                if(records.isEmpty())
                {records = consumer.poll(Duration.ofMillis(4000));
                  System.out.println("\n kafka messageListen LOOP  :  " + records);
                }
                       for (ConsumerRecord<String, String> record : records) {
                String message = record.value();
                System.out.println("\n kafka messageListen records 1 :  " + message);
  
               
                try {
                    root = objectMapper.readTree(message );
                } catch (JsonProcessingException e) {
                    System.out.println("Parsing Error in Listen");
                }   
                 processed = root;
            }
            consumer.close();
           
        } catch ( Exception  e) {
            System.out.println("\n Error occurred in kafka listening" + e.getMessage());
        };

        System.out.println("\n\n Came in kafka messageListen :  " + processed);

        return root;
    }

    @Override
    @Async
    public boolean messageVerify(JsonNode ProcessedOutput, JsonNode ProvidedOutput) {
        try {
                System.out.println("\n 1"+ ProcessedOutput );
                System.out.println("\n 2"+ ProvidedOutput);
                return ProcessedOutput.equals(ProvidedOutput);
        } catch (Exception e) {
            System.out.println("\n Error occurred in kafka verifying" + e.getMessage());
            return false;
        }
    }

    @Override
    @Async
    protected void simulate(String outputTopic, String ProvidedOutput) {
                System.out.println("\n Came in kafka simulate");
        kafkaTemplate.send(outputTopic, ProvidedOutput);
        }

}