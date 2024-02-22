package com.example.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.app.model.TestInput;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class VerficationService {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    KafkaValidationInterfaceImpl kafkaValidationInterfaceImpl;

    public String beginVerification( String jsonData)
    {
        TestInput inputTest = null;
        try {
            inputTest = objectMapper.readValue(jsonData , TestInput.class);
            System.out.println("Value: " + inputTest);
        } catch (JsonProcessingException e) {
            System.out.println(" Json parsing Error " + e);
        }

        String result = kafkaValidationInterfaceImpl.execute(inputTest) == true ? "PASS"  : "FAIL" ;
        System.out.println("TEST RESULT : " + result);

        return result;
    }
    
}
