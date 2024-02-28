package com.example.app.model;


import lombok.Data;

@Data
public class TestInput{

    private String inputTopic;
    private String outputTopic;
    private String databaseName;
    private String containerName;
    private InputMsgJson inputMsgJson;
    private InputMsgJson outputMsgJson;
    
}