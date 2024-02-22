package com.example.app.controller;
import com.example.app.service.impl.VerficationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TestSuiteController {

    @Autowired
    private VerficationService verificationService;

    @PostMapping
    String testKafkaMessage(@RequestBody String jsonData)
    {
        return verificationService.beginVerification(jsonData);
    }
    
}
