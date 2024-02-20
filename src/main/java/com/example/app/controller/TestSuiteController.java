package com.example.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.model.InputTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;



@RestController
public class TestSuiteController {

    @Autowired
    ObjectMapper objectMapper;

    @PostMapping
    InputTest testKafkaMessage(@RequestBody String jsonData)
    {
    
        InputTest jsonNode = null;
        try {
            jsonNode = objectMapper.readValue(jsonData , InputTest.class);
        } catch (JsonProcessingException e) {
            System.out.println(" Json parsing Error " + e);
        }


        System.out.println("Value: " + jsonNode);
        return jsonNode;

    }
    
}
