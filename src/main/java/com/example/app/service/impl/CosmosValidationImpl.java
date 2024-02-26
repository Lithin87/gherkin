package com.example.app.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.azure.cosmos.CosmosClient;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.CosmosDatabase;
import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.util.CosmosPagedIterable;
import com.example.app.service.ValidationTemplateInterface;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.app.model.InputMsgJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service("cosmos")
public class CosmosValidationImpl extends ValidationTemplateInterface {

    private static final Logger logger = LoggerFactory.getLogger(CosmosValidationImpl.class);


    @Autowired
    private final KafkaTemplate<String, String> kafkaTemplate = null;

     private CosmosDatabase database;
    private CosmosContainer container;

    @Value("${azure.cosmos.uri}")
    private String cosmosUri;

    @Value("${azure.cosmos.key}")
    private String cosmosKey;   

    @Value("${azure.cosmos.database}")
    private String databaseName;

    @Value("${azure.cosmos.container}")
    private String containerName;   

    @Autowired
    ObjectMapper objectMapper;

    private List<InputMsgJson> processed = new ArrayList<InputMsgJson>();

    @Override
    protected void messageSend(String inputTopic, String jsonContent) {
        kafkaTemplate.send(inputTopic, jsonContent);
        logger.info("\n At cosmos sending ");
    }


    @Override
    protected List<InputMsgJson> messageListen(String outputTopic) {

        String[] databaseLocation = outputTopic.split(":");

        String databaseName = databaseLocation[0]; 
        String containerName = databaseLocation[1]; 

        try {
            Thread.sleep(2000);
        CosmosClient client = new CosmosClientBuilder().endpoint(cosmosUri).key(cosmosKey).buildClient();
        database  = client.getDatabase(databaseName);
        container  = database.getContainer(containerName);
        System.out.println("Querying items.");
        
        CosmosPagedIterable<InputMsgJson> queryResults  = container.queryItems(
        "SELECT * FROM c ", new CosmosQueryRequestOptions(), InputMsgJson.class);

        Thread.sleep(3000);
        queryResults.forEach(processed::add); 

        } catch (Exception e) {
            logger.error("\n Error occurred in cosmos listening" + e.getMessage());
        }
        logger.info("\n \n Final listener retreived items."+processed);
        return processed;
    }

    @Override
    public boolean messageVerify(List<InputMsgJson> ProcessedOutput, InputMsgJson ProvidedOutput) {
        try {
            Thread.sleep(5000);

            if (processed == null) {
                System.out.println("\n No Output message received");
                return false;
            } else {
                System.out.println("\n 1"+ processed );
                System.out.println("\n 2"+ ProvidedOutput);
                return processed.contains(ProvidedOutput);
            }
        } catch (Exception e) {
            logger.error("\n Error occurred in cosmos verifying" + e.getMessage());
            return false;
        }
    }

    @Override
    protected void simulate(String outputTopic, String ProvidedOutput) {
        System.out.println("\n Came in cosmos simulate ");

        String[] databaseLocation = outputTopic.split(":");

        String databaseName = databaseLocation[0]; 
        String containerName = databaseLocation[1]; 


        CosmosClient client = new CosmosClientBuilder().endpoint(cosmosUri).key(cosmosKey).buildClient();
        try {
     
        client.createDatabaseIfNotExists(databaseName);
        database  = client.getDatabase(databaseName);

        database.createContainerIfNotExists(containerName, "/articleNumber");
        container  = database.getContainer(containerName);

        InputMsgJson root = objectMapper.readValue(ProvidedOutput, InputMsgJson.class);
        
        container.createItem(root);
        System.out.println("\n Passed value "+ProvidedOutput);
     

        } catch ( Exception e)
        {
            logger.error("Simulate Persisting Error."+ e);
        }

    }

}