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
    
    protected  abstract void messageSend(String inputTopic, String jsonContent);
    protected  abstract void simulate(String outputTopic, String ProvidedOutput);
    protected  abstract List<JsonNode> messageListen(String outputTopic);
    protected  abstract boolean messageVerify(List<JsonNode>  ProcessedOutput, JsonNode ProvidedOutput);

    public  final  boolean execute( TestInput testInput ) {
    
      try {

        String inputTopic = testInput.getInputTopic();
        String outputTopic = testInput.getOutputTopic();
      
        JsonNode inputMsgJson = testInput.getInputMsgJson();
        JsonNode providedOutput = testInput.getOutputMsgJson(); 
        List<JsonNode> processedOutput =  null;

     
        if(outputTopic == null)
         {  
            String dbName = testInput.getDatabaseName();
            String tbleName= testInput.getContainerName();
            String id = providedOutput.get("id").asText();
            outputTopic = String.join(":", dbName, tbleName, id);

            messageSend( inputTopic, objectMapper.writeValueAsString(inputMsgJson));
            simulate(outputTopic ,  objectMapper.writeValueAsString(providedOutput));
            processedOutput = messageListen( outputTopic);
            return messageVerify( processedOutput,  providedOutput);

         }else{
    
            processedOutput = messageListen( outputTopic);
            messageSend( inputTopic, objectMapper.writeValueAsString(inputMsgJson));
            simulate(outputTopic ,  objectMapper.writeValueAsString(providedOutput));
            return messageVerify( processedOutput,  providedOutput);
         }
            } catch (JsonProcessingException e) {
            System.out.println("Exception in JSON Parsing " + e );
            return false;
        }
    }
}


    