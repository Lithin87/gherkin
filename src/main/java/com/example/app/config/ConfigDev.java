package com.example.demo.config;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.core.KafkaTemplate;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;

@Configuration
@Profile("dev")
public class ConfigDev {
    
String TopicVerify;

@Value("${spring.kafka.bootstrap-servers}")
private String bootstrapServers = "";

@Value("${spring.kafka.client-id}")
private String clientID;

@Value("${spring.kafka.size}")
private String kafkasize;

@Value("${spring.kafka.consumer.promotion.topicName}")
private String topicName = "";

@Value("${spring.kafka.consumer.group-id}")
private String kafkaConsumerGroupid = "";

@Value("${spring.kafka.properties.security.protocol}")
private String saslssl = "";

@Value("${spring.kafka.properties.sasl.mechanism}")
private String plain = "";

@Value("${spring.kafka.properties.ssl.endpoint.identification.algorithm}")
private String https = "";

@Value("${spring.kafka.properties.sasl.jaas.config}")
private String kafkaJassConfigUri;

@Value("${spring.kafka.username-path}")
private String kafkaUserNamePath;

@Value("${spring.kafka.password-path}")
private String kafkaPasswordPath;


@Bean
public ConsumerFactory<String, String> consumerFactory() {
    Map<String, Object> config = new HashMap<>();
    System.out.println("*************Kafka_Connected*************");
    config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    config.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConsumerGroupid);
    config.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, saslssl);
    config.put(SslConfigs.DEFAULT_SSL_ENDPOINT_IDENTIFICATION_ALGORITHM, https);
    config.put(SaslConfigs.SASL_MECHANISM, plain);
    config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    config.put(SaslConfigs.SASL_JAAS_CONFIG, getKafkaJassConfig());
    config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), new StringDeserializer());
}

@Bean
public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(KafkaOperations<String, String> template) {
    ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory());
    return factory;
}

private String getKafkaJassConfig() {
    String kafkaJaasConfig = kafkaJassConfigUri.replace("{username}", "IYSK75EVY5VMW24A") ;
    kafkaJaasConfig = kafkaJaasConfig.replace("{password}","Xcu1yMN7jThM7icPWM+/SJjb0p40inJIygYpm+6OhAB5BLV64IWVyH7iMB7M5lGS");
    return kafkaJaasConfig;
}


@Bean
public Map<String, Object> producerConfigs(){
    Map<String, Object> producerConfig = new HashMap<>();
    producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);
    producerConfig.put(ProducerConfig.CLIENT_ID_CONFIG, clientID);
    producerConfig.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, saslssl);
    producerConfig.put(SslConfigs.DEFAULT_SSL_ENDPOINT_IDENTIFICATION_ALGORITHM, https);
    producerConfig.put(SaslConfigs.SASL_MECHANISM, plain);
    producerConfig.put(SaslConfigs.SASL_JAAS_CONFIG, getKafkaJassConfig());
    producerConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    producerConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    producerConfig.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, kafkasize);
    return producerConfig;
}


@Bean
public KafkaTemplate<String, String> kafkaTemplate()  {
    return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(producerConfigs()));
}


}

