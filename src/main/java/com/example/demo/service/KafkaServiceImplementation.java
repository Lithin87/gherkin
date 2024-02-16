package com.example.demo.service;

import com.example.demo.cosmosdb.CosmosDBService;
import com.example.demo.dto.RootDto;
import com.example.demo.kafkaService.KafkaSenderService;
import com.example.demo.model.Root;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class KafkaServiceImplementation {

    @Autowired
    private KafkaSenderService kafkaSend;

    @Autowired
    private CosmosDBService cosmosDBService;

    @Autowired
    private KafkaTransformerService kafkaTransformerService;

    @Value("${spring.kafka.consumer.partition.outputTopic}")
    private String outputTopicName;

    private ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public void specificPartition(String kafkaInput) throws JsonProcessingException {
        RootDto rootDto = new RootDto(); 
        Root root = objectMapper.readValue(kafkaInput, Root.class);
        String articleNumber = root.getArticleNumber();

        rootDto.setTableNumber(root.getTableNumber());
        rootDto.setArticleNumber(articleNumber);
        rootDto.setSalesOrganisation(root.getSalesOrganisation());
        rootDto.setConditionRecordNumber(root.getConditionRecordNumber());
        rootDto.setValidFrom(root.getValidFrom());
        rootDto.setValidTo(root.getValidTo());
        rootDto.setConditionType(root.getConditionType());
        rootDto.setCreatedTimestamp(root.getCreatedTimestamp());
        rootDto.setEventType(root.getEventType());
        rootDto.setUpdatedTimestamp((String) root.getUpdatedTimestamp()); // Check type
        rootDto.setTtl(root.getTtl());

        if (articleNumber != null) {
           
            rootDto.setTableNumber(root.getTableNumber());
            rootDto.setArticleNumber(articleNumber);

            if ("A071".equals(root.getTableNumber())) {
                if ("INSERT".equals(root.getEventType())) {
                    articleNumber = articleNumber.replaceFirst("^0+", "");
                    rootDto.setEventType("INSERT");
                    kafkaSend.sendMessage(outputTopicName, 1, articleNumber, objectMapper.writeValueAsString(rootDto));
                } else {
                    kafkaSend.sendMessage(outputTopicName, 0, articleNumber, objectMapper.writeValueAsString(rootDto));
                }
            } else {
                kafkaSend.sendMessage(outputTopicName, 2, articleNumber, objectMapper.writeValueAsString(rootDto));
            }
        }
    }
 
    public void cosmosTransformPersist(String kafkaInput) throws IOException {
       
        Map<String, String> fieldList = new HashMap<>();
        fieldList.put("eventType", "INSERT");
        fieldList.put("articleType", "cosmos");
        fieldList.put("salesOrganisation", "qrech");
        fieldList.put("customerNumber", "WBA");

        String transformedKafkaInput =  kafkaTransformerService.selectiveFieldUpdation(kafkaInput, fieldList);
        Root root = objectMapper.readValue(transformedKafkaInput, Root.class);
        cosmosDBService.persist(root );

    }

}


