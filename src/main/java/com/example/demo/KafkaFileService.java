package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.core.io.Resource;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.io.IOException;

@Service
public class KafkaFileService {

    @Autowired
    private final KafkaTemplate<String, String> kafkaTemplate = null;

    @Autowired
    private  KafkaConsumerService kafkaConsumerService ;

    @Autowired
    private final KafkaTransformerService   kafkaTransformerService = null;

    
    public void sendMessage(String topic, String fileName) {

        try {
            Resource resource = new ClassPathResource(fileName);
            byte[] fileBytes = Files.readAllBytes(Paths.get(resource.getURI()));
            String fileContent = new String(fileBytes);

            kafkaTemplate.send(topic, fileContent);
            System.out.println("File Content sent successfully to Kafka topic: " + topic);
        } catch (IOException e) {
           System.out.println("Error Occured while sending file message"+ e);
        }
    }

    public void consumeProcessSendMessage(String topicS , String topicD)  {

        try {
            kafkaConsumerService.unsubscribeConsumerFromTopic();
            kafkaConsumerService.subscribeConsumerToTopic(topicS);
            List<String> allMessage = kafkaConsumerService.getAllMessage();
            String lastNode = allMessage.get(allMessage.size()-1) ;
            String transformedString = kafkaTransformerService.eventTypeUpdation(lastNode);

            kafkaTemplate.send(topicD, transformedString);
        } catch (IOException e) {
            System.out.println("Error Occured while transforming and sending file message"+ e);
        }

    }


    public boolean verifyConsumerReceivesMessage(String fileD )  {

        try {
            Resource resource = new ClassPathResource(fileD);
            byte[] fileBytes = Files.readAllBytes(Paths.get(resource.getURI()));
            String fileContent = new String(fileBytes);

            return kafkaConsumerService.checkMessage(fileContent);

        } catch (IOException e) {
            System.out.println("Error Occured while verifying transformation of file message"+ e);
        }
        return false;

    }
    
}
 