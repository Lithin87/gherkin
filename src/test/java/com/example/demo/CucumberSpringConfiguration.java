package com.example.demo;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;



@CucumberContextConfiguration
@ContextConfiguration(classes = TestApplication.class) // Specify the main application class
@SpringBootTest
public class CucumberSpringConfiguration {
}
