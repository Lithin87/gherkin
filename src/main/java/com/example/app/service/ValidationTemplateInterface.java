package com.example.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.app.model.TestInput;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.app.model.InputMsgJson;


public abstract class ValidationTemplateInterface {

     @Autowired
    ObjectMapper objectMapper;
    
    protected  abstract void messageSend(String inputTopic, String outputTopic, String jsonContent);
    protected  abstract void simulate(String outputTopic, String ProvidedOutput);
    protected  abstract JsonNode messageListen(String outputTopic);
    protected  abstract boolean messageVerify(JsonNode  ProcessedOutput, JsonNode ProvidedOutput);

    public  final  boolean execute( TestInput testInput ) {
    
      try {

        String inputTopic = testInput.getInputTopic();
        String outputTopic = testInput.getOutputTopic();
      
        JsonNode inputMsgJson = testInput.getInputMsgJson();
        JsonNode providedOutput = testInput.getOutputMsgJson(); 
        JsonNode processedOutput =  null;

     
        if(outputTopic == null)
         {  
            String dbName = testInput.getDatabaseName();
            String tbleName= testInput.getContainerName();
            String id = providedOutput.get("id").asText();
            outputTopic = String.join(":", dbName, tbleName, id);

            messageSend( inputTopic, outputTopic, objectMapper.writeValueAsString(inputMsgJson));
            simulate(outputTopic ,  objectMapper.writeValueAsString(providedOutput));
            processedOutput = messageListen( outputTopic);
            return messageVerify( processedOutput,  providedOutput);

         }else{
    
            messageSend( inputTopic, outputTopic,  objectMapper.writeValueAsString(inputMsgJson));
            simulate(outputTopic ,  objectMapper.writeValueAsString(providedOutput));
            processedOutput = messageListen( outputTopic);
            return messageVerify( processedOutput,  providedOutput);
         }
            } catch (JsonProcessingException e) {
            System.out.println("Exception in JSON Parsing " + e );
            return false;
        }
    }
}


    