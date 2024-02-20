package com.example.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.model.TestInput;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;



@RestController
public class TestSuiteController {

    @Autowired
    ObjectMapper objectMapper;

    @PostMapping
    TestInput testKafkaMessage(@RequestBody String jsonData)
    {
    
        TestInput inputTest = null;
        try {
            inputTest = objectMapper.readValue(jsonData , TestInput.class);
        } catch (JsonProcessingException e) {
            System.out.println(" Json parsing Error " + e);
        }


        System.out.println("Value: " + inputTest);
        return inputTest;

    }
    
}
