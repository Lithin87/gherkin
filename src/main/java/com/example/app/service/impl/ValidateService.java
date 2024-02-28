package com.example.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.example.app.model.TestInput;
import com.example.app.service.ValidationTemplateInterface;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ValidateService {

    @Autowired
    ObjectMapper objectMapper;
    
    @Autowired
    private ApplicationContext context;
    
    
    public String beginVerification( String jsonData)
    {
        TestInput inputTest = null;
        try {
            inputTest = objectMapper.readValue(jsonData , TestInput.class);
            System.out.println("Value: " + inputTest);

            String qualifier =  (inputTest.getOutputTopic() == null) ? "cosmos" : "kafka" ;
            ValidationTemplateInterface validationTemplateImpl = (ValidationTemplateInterface) context.getBean(qualifier);

            String result = validationTemplateImpl.execute(inputTest) == true ? "TEST PASS"  : "TEST FAIL" ;
            System.out.println("TEST RESULT : " + result);
            return result;
        } catch (Exception e) {
            System.out.println(" Parsing Error " + e);
            return "FAIL";
        }
    }
    
}
