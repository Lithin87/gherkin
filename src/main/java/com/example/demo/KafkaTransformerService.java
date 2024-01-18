package com.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;


@Service
public class KafkaTransformerService {

    @Autowired
    private final ObjectMapper objectMapper = null;

    public String eventTypeUpdation(String message) throws IOException {

        JsonNode jsonNode = objectMapper.readTree(message);

        if (jsonNode.has("eventType")) {
            String eventType = jsonNode.get("eventType").asText();
            if ("INSERT".equals(eventType)) {
                ((ObjectNode) jsonNode).put("articleNumber", "7777777");
            } else if ("UPDATE".equals(eventType)) {
                ((ObjectNode) jsonNode).put("articleNumber", "888888");
            }
        }
        String modifiedJsonString = objectMapper.writeValueAsString(jsonNode);
        return modifiedJsonString;
    }


    public String selectiveFieldUpdation(String message ,Map<String,String> fieldList) throws IOException {

        JsonNode jsonNode = objectMapper.readTree(message);

        fieldList.entrySet().forEach(s -> {        
                if (jsonNode.has(s.getKey()))  ((ObjectNode) jsonNode).put(s.getKey(), s.getValue());
        });
        
        String modifiedJsonString = objectMapper.writeValueAsString(jsonNode);
        return modifiedJsonString;
    }

}
