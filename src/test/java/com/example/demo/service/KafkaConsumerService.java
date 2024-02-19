package com.example.demo.service;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class KafkaConsumerService {

private List<String> receivedMessages = new ArrayList<>();

    @Autowired
    private final Consumer<String, String> kafkaConsumer = null;

    @Autowired
    private final ObjectMapper objectMapper = null;
    
    Timestamp recordTimestamp;

    public Timestamp getRecordTimestamp() {
        return recordTimestamp;
    }

    public void setRecordTimestamp(Instant recordTimestamp) {
        this.recordTimestamp = Timestamp.from(recordTimestamp);
    }

    public List<String> getReceivedMessages() {
        return receivedMessages;
    }



    public void subscribeConsumerToTopic(String topic , int partition) {
       
        try {
            kafkaConsumer.unsubscribe();
            if(partition > -1)
            kafkaConsumer.assign(Collections.singletonList(new TopicPartition(topic, partition)));
            else
            kafkaConsumer.subscribe(Collections.singletonList(topic));
            System.out.println("Consumer subscribed to Kafka topic: " + topic);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unsubscribeConsumerFromTopic() {
        try {
            kafkaConsumer.unsubscribe();
            System.out.println("Consumer unsubscribed from all topics");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkMessage(String message) {
    
        ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(5000));

            List<String> messages = StreamSupport.stream(records.spliterator(), false)
                    .map(record -> record.value())
                    .collect(Collectors.toList());

                    receivedMessages.addAll(messages);
                    System.out.println("\n TOTAL MSG : " + receivedMessages );
                    String lastMessage = "";
                    try {
                        lastMessage = receivedMessages.get(receivedMessages.size() -1 );
                  
                        System.out.println("\n => " + lastMessage + " === " + message);

                        JsonNode node1 = objectMapper.readTree(lastMessage);
                        JsonNode node2 = objectMapper.readTree(message);

                        if (node1.equals(node2)) {
                            return true;
                        } else {
                            return false;
                        }
                    }catch (Exception e)
                    {
                        System.out.println("\n NOT JSON => " + lastMessage + " === " + message);
                        if(lastMessage.equals(message))
                        return true;
                        else
                        return false;
                    }
        }


        public List<String> getAllMessage() {
    
            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(5000));
    
                List<String> messages = StreamSupport.stream(records.spliterator(), false)
                        .map(record -> record.value())
                        .collect(Collectors.toList());
    
                         receivedMessages.addAll(messages);
    
                         return receivedMessages;
            }
    
}
 