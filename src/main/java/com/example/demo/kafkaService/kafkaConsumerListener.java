package com.example.demo.kafkaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.example.demo.service.KafkaServiceImplementation;


@Service
public class kafkaConsumerListener {
    @Autowired
    KafkaServiceImplementation kafkaServiceImplementation;

    @KafkaListener(topics = "${spring.kafka.consumer.promotion.topicName}", groupId = "${spring.kafka.consumer.service.group-id}")
    public void listen( String KafkaInput) throws Exception {
        System.out.println("Received Message:"+KafkaInput);
        kafkaServiceImplementation.passpayload(KafkaInput);
    }

    @KafkaListener(topics = "${spring.kafka.consumer.cosmos.topicName}", groupId = "${spring.kafka.consumer.cosmos.group-id}")
    public void listen1( String KafkaInput) throws Exception {
        try {System.out.println("Received Message cosmos :"+KafkaInput);
        kafkaServiceImplementation.cosmosPersist(KafkaInput);
    } catch ( Exception e)
    {
        System.out.println("Cosmos Exception " + e);
    }
    }


}

