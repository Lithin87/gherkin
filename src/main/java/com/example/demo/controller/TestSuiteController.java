package com.example.demo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
public class TestSuiteController {

    @PostMapping
    String testKafkaMessage(@RequestBody String jsonData)
    {
        // String jsonString = "{\"key\": \"value\"}"; 

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(jsonData);
        } catch (JsonProcessingException e) {
            System.out.println(" Json parsing Error " + e);
        }



        String value = jsonNode.get("key").asText();
        System.out.println("Value: " + value);
        return value;

    }
    
}
