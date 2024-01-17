package com.example.demo;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import org.springframework.beans.factory.annotation.Autowired;

public class KafkaStepDefinitions {

    @Autowired
    private KafkaSenderService kafkaSenderService;

    @Autowired
    private KafkaFileService kafkaFileService;

    @Autowired
    private  KafkaConsumerService kafkaConsumerService ;

    @Given("a Kafka topic {string} exists in {string}")
    public void createKafkaTopic(String topic,String bootstrapServers) {

        AdminClientTest adminClientTest = new AdminClientTest();
        adminClientTest.checkTopic( bootstrapServers , topic);
    }


    @When("a message {string} is sent to {string}")
    public void sendMessageToKafka(String message, String topic) {
        kafkaSenderService.sendMessage(topic, message);
    }

    @Then("the message {string} is successfully written to {string}")
    public void verifyMessageProduced(String expectedMessage, String topic) {

        kafkaConsumerService.subscribeConsumerToTopic(topic);
        boolean messageProduced = kafkaConsumerService.checkMessage(expectedMessage);
        if (messageProduced) {
            System.out.println("Message was successfully produced to Kafka topic");
        } else {
            System.out.println("Message was not produced to Kafka topic");
        }
    }

    
    
    
    @And("a message {string} is present in {string}")
    public void messagePresent(String expectedMessage, String topic) {
        boolean messageProduced = kafkaConsumerService.checkMessage(expectedMessage);
        if (messageProduced) {
            System.out.println("The Message is present in Kafka topic");
        } else {
            System.out.println("The Message is not present in Kafka topic");
        }
    }
    
    @When("a consumer subscribes to {string}")
    public void subscribeConsumerToTopic(String topic) {
        kafkaConsumerService.subscribeConsumerToTopic(topic);
    }
    
    
    @Then("the consumer receives the message {string} from {string}")
    public void verifyConsumerReceivesMessage(String expectedMessage , String topic) {

        boolean messageProduced = kafkaConsumerService.checkMessage(expectedMessage);
        if (messageProduced) {
            System.out.println("The Message is present in Kafka topic");
        } else {
            System.out.println("The Message is not present in Kafka topic");
        }
    }


    @When("a file {string} is sent to {string}")
    public void sendFileToKafka(String file, String topic) {
        kafkaFileService.sendMessage(topic, file);
    }
    

    @When("json is retrieved from {string} and transformed and sent to {string}")
    public void transformJsonAndSendToKafka(String topicS, String topicD) {
        kafkaFileService.consumeProcessSendMessage(topicS, topicD);
    }



    @Then("the consumer receives the message equivalent to file {string}")
    public void verifyConsumerReceivesMessage(String fileD ) {
        
        boolean messageProduced = kafkaFileService.verifyConsumerReceivesMessage(fileD);
        if (messageProduced) {
            System.out.println("The Transformed Message Kafka topic matches with expected");
        } else {
            System.out.println("The Transformed Message Kafka topic doesnt matches with expected");
        }
    }
    
}

