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
        List<InputMsgJson> processedOutput =  null;

        try {

        if(outputTopic == null)
         {  
            String dbName = testInput.getDatabaseName();
            String tbleName= testInput.getContainerName();
            String id = ProvidedOutput.getId();
            outputTopic = String.join(":", dbName, tbleName, id);

            messageSend( inputTopic, objectMapper.writeValueAsString(InputMsgJson));
            simulate(outputTopic ,  objectMapper.writeValueAsString(ProvidedOutput));
            processedOutput = messageListen( outputTopic);
            return messageVerify( processedOutput,  ProvidedOutput);

         }else{
    
            processedOutput = messageListen( outputTopic);
            messageSend( inputTopic, objectMapper.writeValueAsString(InputMsgJson));
            simulate(outputTopic ,  objectMapper.writeValueAsString(ProvidedOutput));
            return messageVerify( processedOutput,  ProvidedOutput);
         }
            } catch (JsonProcessingException e) {
            System.out.println("Exception in JSON Parsing " + e );
            return false;
        }
    }
}


    