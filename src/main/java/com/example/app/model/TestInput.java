package com.example.app.model;


import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class TestInput{

    private String inputTopic;
    private String outputTopic;
    private String inputMsgJson;
    private String outputMsgJson;
    
}
