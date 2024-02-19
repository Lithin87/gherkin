package com.example.app.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class InputTest{


    public String id;
    public String inputTopic;
    public String outputTopic;
    public String inputMsgJsonList;
    public String outputMsgJsonList;
}
