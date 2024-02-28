package com.example.app.service.impl;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.app.model.InputMsgJson;
import com.example.app.service.ValidationTemplateInterface;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("kafka")
public class KafkaValidationImpl extends ValidationTemplateInterface {

    @Autowired
    private final KafkaTemplate<String, String> kafkaTemplate = null;

    @Autowired
    private ConsumerFactory<String, String> consumerFactory;



    @Autowired
    ObjectMapper objectMapper;

    private List<InputMsgJson> processed = new ArrayList<InputMsgJson>();

    @Override
    protected void messageSend(String inputTopic, String jsonContent) {
        kafkaTemplate.send(inputTopic, jsonContent);
                System.out.println("\n Came in kafka sending ");
    }


    @Override
    protected List<InputMsgJson> messageListen(String outputTopic) {
        try {

      KafkaConsumer<String, String> consumer = (KafkaConsumer<String, String>) consumerFactory.createConsumer();

      consumer.subscribe(Set.of(outputTopic));

      consumer.poll(Duration.ofMillis(100)); 
        for (TopicPartition partition : consumer.assignment()) {
            consumer.seekToEnd(Collections.singleton(partition));
        }

            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(6000));
            for (ConsumerRecord<String, String> record : records) {
                String message = record.value();
                System.out.printf("\n Offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
                InputMsgJson root = null;
                try {
                    root = objectMapper.readValue(message,InputMsgJson.class );
                } catch (JsonProcessingException e) {
                    System.out.println("Parsing Error in Listen");
                   
                }   

                processed.add(root);
            }
    
            consumer.close();
            System.out.println("\n Came in kafka messageListen " + outputTopic);

        } catch ( Exception  e) {
            System.out.println("\n Error occurred in kafka listening" + e.getMessage());
        };
        return processed;
    }

    @Override
    public boolean messageVerify(List<InputMsgJson> ProcessedOutput, InputMsgJson ProvidedOutput) {
        try {
            Thread.sleep(9000);

            if (processed == null) {
                System.out.println("\n No Output message received");
                return false;
            } else {
                System.out.println("\n 1"+ processed );
                System.out.println("\n 2"+ ProvidedOutput);
                return processed.contains(ProvidedOutput);
            }
        } catch (Exception e) {
            System.out.println("\n Error occurred in kafka verifying" + e.getMessage());
            return false;
        }
    }

    @Override
    protected void simulate(String outputTopic, String ProvidedOutput) {
        System.out.println("\n Came in kafka simulate");
        kafkaTemplate.send(outputTopic, ProvidedOutput);
    }

}