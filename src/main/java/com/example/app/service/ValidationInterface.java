package com.example.app.service;



public abstract class ValidationInterface {
    
    public abstract void messageSend(String inputTopic, String jsonContent);

    public abstract void messageListen(String ProcessedOutput);

    public abstract boolean messageVerify(String ProcessedOutput, String ProvidedOutput);

    public final  boolean execute(String inputTopic, String jsonContent,String ProcessedOutput , String ProvidedOutput ) {
        messageSend( inputTopic,  jsonContent);
        messageListen( ProcessedOutput);
        return messageVerify( ProcessedOutput,  ProvidedOutput);
    }
}


    