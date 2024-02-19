package com.example.demo.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Root{


    public String id;
    public String inputTopic;
    public String outputTopic;
    public String inputMsgJsonList;
    public String outputMsgJsonList;
}
