package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.example.demo.cosmosdb.CosmosDBService;

import org.springframework.core.io.Resource;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.io.IOException;

@Service
public class KafkaFileService {

    @Autowired
    private final KafkaTemplate<String, String> kafkaTemplate = null;

    @Autowired
    private  KafkaConsumerService kafkaConsumerService ;

    @Autowired
    private final KafkaTransformerService   kafkaTransformerService = null;

    @Autowired
    private final CosmosDBService cosmosDBService = null;

    
    public void sendMessage(String topic, String fileName) {

        try {
            String fileContent = getFileContent(fileName);
            kafkaTemplate.send(topic, fileContent);
            System.out.println("File Content sent successfully to Kafka topic: " + topic);

        } catch (IOException e) {
           System.out.println("Error Occured while sending file message"+ e);
        }
    }

    public void consumeProcessSendMessage(String topicS , String topicD)  {

        try {
            String lastNode = getLastMessage(topicS);
            String transformedString = kafkaTransformerService.eventTypeUpdation(lastNode);
            kafkaTemplate.send(topicD, transformedString);

        } catch (IOException e) {
            System.out.println("Error Occured while transforming and sending file message"+ e);
        }

    }


    public boolean verifyConsumerReceivesMessage(String fileD )  {

        try {
            String fileContent = getFileContent(fileD);
            return kafkaConsumerService.checkMessage(fileContent);

        } catch (IOException e) {
            System.out.println("Error Occured while verifying transformation of file message"+ e);
        }
        return false;
    }

    public boolean verifyCosmosReceivedMessage(String container , String fileD )  {

        try {
            String fileContent = getFileContent(fileD);
            return cosmosDBService.verify(container , fileContent);

        } catch (IOException e) {
            System.out.println("Error Occured while verifying transformation of file message"+ e);
        }
        return false;
    }

    public void consumeSelectiveTransformSendMessage(String topicS, String topicD, Map<String, String> fieldList) {
        try {
            String lastNode = getLastMessage(topicS);
            String transformedString = kafkaTransformerService.selectiveFieldUpdation(lastNode, fieldList );
           
            kafkaTemplate.send(topicD, transformedString);
        } catch (IOException e) {
            System.out.println("Error Occured while transforming and sending file message"+ e);
        }
        
    }
    
    private String getLastMessage(String topicS) {
        kafkaConsumerService.unsubscribeConsumerFromTopic();
        kafkaConsumerService.subscribeConsumerToTopic(topicS, -1);
        List<String> allMessage = kafkaConsumerService.getAllMessage();
        String lastNode = allMessage.get(allMessage.size()-1) ;
        return lastNode;
    }
    
    private String getFileContent(String fileD) throws IOException {
        Resource resource = new ClassPathResource(fileD);
        byte[] fileBytes = Files.readAllBytes(Paths.get(resource.getURI()));
        String fileContent = new String(fileBytes);
        return fileContent;
    }

}
