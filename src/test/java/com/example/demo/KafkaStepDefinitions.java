package com.example.demo;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class KafkaStepDefinitions {

    @Autowired
    private KafkaSenderService kafkaSenderService;
     @Autowired
    private KafkaMessageVerifier kafkaMessageVerifier;

    @Autowired
    private final KafkaConsumerService kafkaMessageConsumer= null;

    @Given("a Kafka topic {string} exists in {string}")
    public void createKafkaTopic(String topic,String bootstrapServers) {
        Properties adminClientProps = new Properties();
        adminClientProps.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        try (AdminClient adminClient = AdminClient.create(adminClientProps)) {
            if (!isKafkaTopicExists(adminClient, topic)) {
                createTopic(adminClient, topic);
                System.out.println("Kafka topic created: " + topic);
            } else {
                System.out.println("Kafka topic already exists: " + topic);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void createTopic(AdminClient adminClient, String topic) throws ExecutionException, InterruptedException {
        NewTopic newTopic = new NewTopic(topic, 1, (short) 1);
        adminClient.createTopics(Collections.singleton(newTopic)).all().get();

    }

    private boolean isKafkaTopicExists(AdminClient adminClient, String topic) throws ExecutionException, InterruptedException {
        return adminClient.listTopics().names().get().contains(topic);
    }

    @When("a message {string} is sent to {string}")
    public void sendMessageToKafka(String message, String topic) {
        kafkaSenderService.sendMessage(topic, message);
    }

    @Then("the message {string} is successfully written to {string}")
    public void verifyMessageProduced(String expectedMessage, String topic) {
        boolean messageProduced = kafkaMessageVerifier.verifyMessage(topic, expectedMessage);
        if (messageProduced) {
            System.out.println("Message was successfully produced to Kafka topic");
        } else {
            System.out.println("Message was not produced to Kafka topic");
            // You can throw an exception or handle the failure in some other way
        }
    }

        @And("a message {string} is present in {string}")
    public void messagePresent(String expectedMessage, String topic) {
        boolean messageProduced = kafkaMessageVerifier.verifyMessage(topic, expectedMessage);
        if (messageProduced) {
            System.out.println("The Message is present in Kafka topic");
        } else {
            System.out.println("The Message is not present in Kafka topic");
        }
    }


    @When("a consumer subscribes to {string}")
    public void subscribeConsumerToTopic(String topic) {
        kafkaMessageConsumer.subscribeConsumerToTopic(topic);
    }

    @Then("the consumer receives the message {string}")
    public void verifyConsumerReceivesMessage(String expectedMessage) {
        boolean messageReceived = kafkaMessageConsumer.isMessageReceived(expectedMessage);
        if (messageReceived) {
            System.out.println("Consumer received the expected message: " + expectedMessage);
        } else {
            System.out.println("Consumer did not receive the expected message: " + expectedMessage);
            // You can throw an exception or handle the failure in some other way
        }
    }

    }

