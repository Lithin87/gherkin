package com.example.demo;

import java.util.Collections;
import java.util.Properties;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.AdminClientConfig;


public class AdminClientTest {

    public  boolean checkTopic(String bootstrapServers , String topic) {
        Properties adminClientProps = new Properties();
        adminClientProps.put( AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        
        AdminClient adminClient = AdminClient.create(adminClientProps);

            try (adminClient) {

              

                boolean exists = adminClient.listTopics().names().get().contains(topic);
                if (!exists) {

                    NewTopic newTopic = new NewTopic(topic, 1, (short) 1);
            adminClient.createTopics(Collections.singleton(newTopic)).all().get();

                    System.out.println("Kafka topic created: " + topic);
                } else {
                    System.out.println("Kafka topic already exists: " + topic);
                }
            } catch (Exception e) {
                return false;
            }
            return true;
    }
    
}
