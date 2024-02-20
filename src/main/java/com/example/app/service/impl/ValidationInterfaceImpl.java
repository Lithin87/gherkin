package com.example.app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.app.model.TestInput;
import com.example.app.service.ValidationInterface;

@Service
class ValidationInterfaceImpl extends ValidationInterface
{

@Autowired
private final KafkaTemplate<String, String> kafkaTemplate = null;

@Autowired
private List<TestInput> testInput1;

private List<String> processed;


@Override
public void messageSend(String inputTopic, String jsonContent)
{
    kafkaTemplate.send(inputTopic, jsonContent);
}


@Override
@KafkaListener(id = "listener",  topics = "${testInput1.outputTopic}",  groupId = "${spring.kafka.consumer.cosmos.group-id}"
,  containerFactory = "kafkaListenerContainerFactory")
public void messageListen(String ProcessedOutput) {
    processed.add(ProcessedOutput);
}


@Override
public boolean messageVerify(String ProcessedOutput, String ProvidedOutput)
{
    return processed.contains(ProvidedOutput);
}


   
// @KafkaListener(topics = "${topicName}", groupId = "${spring.kafka.consumer.cosmos.group-id}"
// ,  containerFactory = "kafkaListenerContainerFactory" )
// public void listen1( String KafkaInput) throws Exception 
// {
//     try { 
//         System.out.println("Received Message cosmos :"+KafkaInput);
//     String bn  = System.getProperty("spring.kafka.consumer.function");
//     switch(bn) {
//     case "cosmos" :
//       System.out.println("COSMOS 1");
//       kafkaServiceImplementation.cosmosTransformPersist(KafkaInput);
//       break;
//     case "partition" : 
//       System.out.println("partition");
//         kafkaServiceImplementation.specificPartition(KafkaInput);
//         break;
//     default : 
//     System.out.println("COSMOS 2");
//     break;
// }

}