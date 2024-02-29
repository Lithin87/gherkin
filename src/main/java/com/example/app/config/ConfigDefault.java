
package com.example.app.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;


@Configuration
public class ConfigDefault {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.service.group-id}")
    private String consumerGroupId;

    @Value("${spring.kafka.consumer.client-id}")
    private String consumerClientId;

    @Value("${spring.kafka.client-id}")
    private String producerClientID;

    @Value("${spring.kafka.size}")
    private String kafkasize;

    private static final String INPUT_TOPIC_NAME = "inputTopic";
    private static final String OUTPUT_TOPIC_NAME = "outputTopic";
    private static final int PARTITIONS = 3;
    private static final short REPLICATION_FACTOR = 1;




    @Bean
    public NewTopic inputTopic() {
        return new NewTopic(INPUT_TOPIC_NAME, PARTITIONS, (short) REPLICATION_FACTOR);
    }

    @Bean
    public NewTopic outputTopic() {
        return new NewTopic(OUTPUT_TOPIC_NAME, PARTITIONS , (short) REPLICATION_FACTOR); 
    }

    
    @Bean
    @Lazy(false)
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.CLIENT_ID_CONFIG, producerClientID);
        configProps.put(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG, false);
        configProps.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, kafkasize);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    @Lazy(false)
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
        public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
        config.put(ConsumerConfig.CLIENT_ID_CONFIG, consumerClientId);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        return new DefaultKafkaConsumerFactory<String, String>(config, new StringDeserializer(),
                new StringDeserializer());
    }


    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}