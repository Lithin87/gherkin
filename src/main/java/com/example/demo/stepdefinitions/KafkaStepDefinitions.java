package com.example.demo.stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

public class KafkaStepDefinitions {

    // Implement the necessary Kafka-related setup and actions in your step definitions

    @Given("a Kafka topic {string} exists")
    public void createKafkaTopic(String topic) {
        System.out.println("Hi this is DElhi");
        // Logic to create Kafka topic
    }

    @When("a message {string} is sent to {string}")
    public void sendMessageToKafka(String message, String topic) {
          System.out.println("Hi this is DElhi1");
        // Logic to send message to Kafka
    }

    @Then("the message {string} is successfully produced to {string}")
    public void verifyMessageProduced(String message, String topic) {
         System.out.println("Hi this is DElhi2");
        // Logic to verify message is produced
    }

    @Given("a message {string} is present in {string}")
    public void addMessageToKafka(String message, String topic) {
         System.out.println("Hi this is DElhi3");
        // Logic to add a message to Kafka
    }

    @When("a consumer subscribes to {string}")
    public void subscribeConsumerToTopic(String topic) {
         System.out.println("Hi this is DElhi4");
        // Logic to subscribe consumer to Kafka topic
    }

    @Then("the consumer receives the message {string}")
    public void verifyConsumerReceivesMessage(String expectedMessage) {
         System.out.println("Hi this is DElhi5");
        // Logic to verify consumer receives the expected message
    }

    // Additional step definitions for multiple messages scenario...
}
