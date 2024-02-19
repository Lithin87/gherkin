package com.example.demo;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import org.junit.Assert;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import com.example.demo.kafkaService.KafkaSenderService;
import com.example.demo.service.KafkaConsumerService;
import com.example.demo.service.KafkaFileService;
import com.example.demo.service.KafkaServiceImplementation;


public class KafkaStepDefinitions {

    @Autowired
    private KafkaSenderService kafkaSenderService;

    @Autowired
    private KafkaFileService kafkaFileService;

    @Autowired
    private  KafkaConsumerService kafkaConsumerService ;

    @Autowired
    KafkaServiceImplementation kafkaServiceImplementation;

    private Scenario scenario;


    @Before
    public void before(Scenario scenario) {
        this.scenario = scenario;
        scenario.log("testing started on "+scenario.getName());
    }
    @After
    public void after(Scenario scenario) {
        scenario.log("completed "+ scenario.getName());
    }

    @Given("a Kafka topic {string} exists in {string}")
    public void createKafkaTopic(String topic,String bootstrapServers) {

        AdminClientTest adminClientTest = new AdminClientTest();
        boolean checkTopic = adminClientTest.checkTopic( bootstrapServers , topic);

        if (!checkTopic) {
            Assert.fail("Topic is not created");
        }

    }


    @When("a message {string} is sent to {string}")
    public void sendMessageToKafka(String message, String topic) {
        kafkaSenderService.sendMessage(topic, message);
    }

    @Then("the message {string} is successfully written to {string}")
    public void verifyMessageProduced(String expectedMessage, String topic) {

        kafkaConsumerService.subscribeConsumerToTopic(topic, -1);
        boolean messageProduced = kafkaConsumerService.checkMessage(expectedMessage);
        if (messageProduced) {
            System.out.println("Message was successfully produced to Kafka topic");
            scenario.log("Message was successfully produced to Kafka topic");
        } else {
            System.out.println("Message was not produced to Kafka topic");
            Assert.fail("Message was not produced to Kafka topic");
        }
    }

    
    
    
    @And("a message {string} is present in {string}")
    public void messagePresent(String expectedMessage, String topic) {
        boolean messageProduced = kafkaConsumerService.checkMessage(expectedMessage);
        if (messageProduced) {
            System.out.println("The Message is present in Kafka topic");
            scenario.log("The Message is present in Kafka topic");
            scenario.log("the available messages are " + kafkaConsumerService.getReceivedMessages());
            scenario.log("the last updated time is " + kafkaConsumerService.getRecordTimestamp());
        } else {
            System.out.println("The Message is not present in Kafka topic");
            scenario.log("the available messages are " + kafkaConsumerService.getReceivedMessages());
            scenario.log("the last updated time is " + kafkaConsumerService.getRecordTimestamp());
            Assert.fail("The Message is not present in Kafka topic");
        }
    }
    
    @When("a consumer subscribes to {string}")
    public void subscribeConsumerToTopic(String topic) {
        kafkaConsumerService.subscribeConsumerToTopic(topic, -1);
    }
    
    @When("a consumer subscribes to {string} from partition {int}")
    public void subscribeConsumerToTopicPartition(String topic, Integer partition ) {
        kafkaConsumerService.subscribeConsumerToTopic(topic, partition);
    }
    
    @Then("the consumer receives the message {string} from {string}")
    public void verifyConsumerReceivesMessage(String expectedMessage , String topic) {

        boolean messageProduced = kafkaConsumerService.checkMessage(expectedMessage);
        if (messageProduced) {
            System.out.println("The Message is present in Kafka topic");
            scenario.log("The Message is present in Kafka topic");
        } else {
            System.out.println("The Message is not present in Kafka topic");
            Assert.fail("The Message is not present in Kafka topic");
        }
    }


    @When("a MX1 sends {string} to {string}")
    public void sendFileToKafka(String file, String topic) {
        kafkaFileService.sendMessage(topic, file);
    }
    

    @When("json is retrieved from {string} and transformed and sent to {string}")
    public void transformJsonAndSendToKafka(String topicS, String topicD) {
        kafkaFileService.consumeProcessSendMessage(topicS, topicD);
    }

    @When("MX2 retrieves from {string} and does selective transformation with below data and sends to {string}:")
    public void selectiveFieldUpdation(String topicS, String topicD , DataTable dataTable) {

        Map<String, String> messages = dataTable.asMap(String.class, String.class);
        kafkaFileService.consumeSelectiveTransformSendMessage(topicS, topicD,messages);

        messages.entrySet().forEach(s -> {  System.out.println(s.getKey() + "--" + s.getValue()); });
    }

    @Then("the consumer receives the message equivalent to file {string}")
    public void verifyConsumerReceivesMessage(String fileD ) {
        
        boolean messageProduced = kafkaFileService.verifyConsumerReceivesMessage(fileD);
        if (messageProduced) {
            System.out.println("The Transformed Message from Kafka topic matches with expected");
            scenario.log("The Transformed Message from Kafka topic matches with expected");
        } else {
            System.out.println("The Transformed Message Kafka topic doesn't match with expected");
            Assert.fail("The Transformed Message Kafka topic doesn't match with expected");
        }
    }

    

    @When("a consumer fetches from cosmosdb container {string} it should be equivalent to file {string}")     
    public void verifyCosmosUpdation(String container , String fileD) {
        
        boolean messageProduced = kafkaFileService.verifyCosmosReceivedMessage(container , fileD);
        if (messageProduced) {
            System.out.println("The Cosmos DB Message matches with expected");
            scenario.log("The Cosmos DB Message matches with expected");
        } else {
            System.out.println("The Cosmos DB Message doesn't matches with expected");
            Assert.fail("The Cosmos DB Message doesn't matches with expected");
        }
    }


    @When("a kafka listener listens at topic {string} with group-id {string} for {string}")
    public void fetchFromCosmosDBContainer(String topicsName, String groupId, String partition) throws InterruptedException {

        System.setProperty("spring.kafka.consumer.cosmos.topicName", topicsName);
        System.setProperty("spring.kafka.consumer.cosmos.group-id", groupId);
        System.setProperty("spring.kafka.consumer.function", partition);
        Thread.sleep(2000);
    }

    
    @KafkaListener(topics = "${spring.kafka.consumer.cosmos.topicName}", groupId = "${spring.kafka.consumer.cosmos.group-id}"
    ,  containerFactory = "kafkaListenerContainerFactory" )
    public void listen1( String KafkaInput) throws Exception 
    {
        try { 
            System.out.println("Received Message cosmos :"+KafkaInput);
        String bn  = System.getProperty("spring.kafka.consumer.function");
        switch(bn) {
        case "cosmos" :
          System.out.println("COSMOS 1");
          kafkaServiceImplementation.cosmosTransformPersist(KafkaInput);
          break;
        case "partition" : 
          System.out.println("partition");
            kafkaServiceImplementation.specificPartition(KafkaInput);
            break;
        default : 
        System.out.println("COSMOS 2");
        break;
    }
    } catch ( Exception e)
    {
        System.out.println("Cosmos Exception " + e);
    }
  
}

}


