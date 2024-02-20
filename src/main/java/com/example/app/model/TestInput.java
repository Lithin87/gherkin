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


    public String inputTopic;
    public String outputTopic;
    public String inputMsgJsonList;
    public String outputMsgJsonList;
}
