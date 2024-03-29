package com.example.app.controller;
import com.example.app.service.impl.ValidateService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TestSuiteController {

    @Autowired
    private ValidateService verificationService;

    @PostMapping
    String testKafkaMessage(@RequestBody String jsonData)
    {
        return verificationService.beginVerification(jsonData);
    }
    
}
