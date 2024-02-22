package com.example.app.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.stereotype.Component;

@Component
public class KafkaListenerManager {

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    public void stopListener(String listenerId) {
        kafkaListenerEndpointRegistry.getListenerContainer(listenerId).stop();
    }

    public void startListener(String listenerId) {
        kafkaListenerEndpointRegistry.getListenerContainer(listenerId).start();
    }
}
