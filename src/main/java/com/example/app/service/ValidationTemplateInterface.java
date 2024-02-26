package com.example.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.app.model.TestInput;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.app.model.InputMsgJson;


public abstract class ValidationTemplateInterface {

     @Autowired
    ObjectMapper objectMapper;
    
   
    protected  abstract void messageSend(String inputTopic, String jsonContent);
    
    
    protected  abstract void simulate(String outputTopic, String ProvidedOutput);

   
    protected  abstract List<InputMsgJson> messageListen(String outputTopic);

   
    protected  abstract boolean messageVerify(List<InputMsgJson>  ProcessedOutput, InputMsgJson ProvidedOutput);

    public  final  boolean execute( TestInput testInput ) {
    
        String inputTopic = testInput.getInputTopic();
        String outputTopic = testInput.getOutputTopic();
        InputMsgJson InputMsgJson = testInput.getInputMsgJson();
        InputMsgJson ProvidedOutput = testInput.getOutputMsgJson();
        if(outputTopic == null)
         {  
            String databaseLocation = String.join(":", testInput.getDatabase()); 
           outputTopic = databaseLocation;
         }
    
        List<InputMsgJson> processedOutput =  null;
        try {
            messageSend( inputTopic, objectMapper.writeValueAsString(InputMsgJson));
            simulate(outputTopic ,  objectMapper.writeValueAsString(ProvidedOutput));
            processedOutput = messageListen( outputTopic);
        
            } catch (JsonProcessingException e) {
            System.out.println("Exception in JSON PArsing " + e );
        }
        return messageVerify( processedOutput,  ProvidedOutput);
    }
}


    