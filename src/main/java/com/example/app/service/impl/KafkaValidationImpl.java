package com.example.app.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
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
    private ConcurrentKafkaListenerContainerFactory<String, String> listenerContainerFactory;

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
        
            ConcurrentMessageListenerContainer<String, String> container = listenerContainerFactory
                    .createContainer(outputTopic);

            MessageListener<String, String> messageListener = record -> {
                String message = record.value();
                InputMsgJson root = null;
                try {
                    root = objectMapper.readValue(message,InputMsgJson.class );
                } catch (JsonProcessingException e) {
                   
                }            
                processed.add(root);
            };

            container.setupMessageListener(messageListener);
            container.start();

            System.out.println("\n Came in kafka messageListen " + outputTopic);

        } catch ( Exception  e) {
            System.out.println("\n Error occurred in kafka listening" + e.getMessage());
        }
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