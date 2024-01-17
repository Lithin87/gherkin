package com.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class KafkaTransformerService {

    public String eventTypeUpdation(String message) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
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
}
