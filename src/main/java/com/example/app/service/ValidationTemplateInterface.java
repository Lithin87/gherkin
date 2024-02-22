package com.example.app.service;

import java.util.List;

import com.example.app.model.TestInput;

public abstract class ValidationTemplateInterface {
    
   
    protected  abstract void messageSend(String inputTopic, String jsonContent);
    
    
    protected  abstract void simulate(String outputTopic, String ProvidedOutput);

   
    protected  abstract List<String> messageListen(String outputTopic);

   
    protected  abstract boolean messageVerify(List<String>  ProcessedOutput, String ProvidedOutput);

    public  final  boolean execute( TestInput testInput ) {
    
        String inputTopic = testInput.getInputTopic();
        String outputTopic = testInput.getOutputTopic();
        String InputMsgJson = testInput.getInputMsgJson();
        String ProvidedOutput = testInput.getOutputMsgJson();
    
        List<String> ProcessedOutput = messageListen( outputTopic);
        messageSend( inputTopic,  InputMsgJson);
        simulate(outputTopic , ProvidedOutput);
        return messageVerify( ProcessedOutput,  ProvidedOutput);
    }
}


    