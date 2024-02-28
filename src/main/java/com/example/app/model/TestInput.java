package com.example.app.model;


import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;

@Data
public class TestInput{

    private String inputTopic;
    private String outputTopic;
    private String databaseName;
    private String containerName;
    private JsonNode inputMsgJson;
    private JsonNode outputMsgJson;
    
}