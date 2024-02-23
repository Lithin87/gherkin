package com.example.app.model;


import java.util.List;

import lombok.Data;

@Data
public class TestInput{

    private String inputTopic;
    private String outputTopic;
    private InputMsgJson inputMsgJson;
    private InputMsgJson outputMsgJson;
    private List<String> database;
    
}