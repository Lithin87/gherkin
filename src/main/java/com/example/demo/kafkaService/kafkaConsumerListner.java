package com.example.demo.kafkaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.example.demo.service.KafkaServiceImplementation;


@Service
public class kafkaConsumerListner {
    @Autowired
    KafkaServiceImplementation kafkaServiceImplementation;
    @KafkaListener(topics = "${spring.kafka.consumer.promotion.topicName}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen( String KafkaInput) throws Exception {
        System.out.println("Received Message:"+KafkaInput);
        kafkaServiceImplementation.passpayload(KafkaInput);
    }}

