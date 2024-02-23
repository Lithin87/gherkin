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

@Service("cosmos")
public class CosmosValidationImpl extends ValidationTemplateInterface {

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

    private List<String> processed = new ArrayList<String>();

    @Override
    protected void messageSend(String inputTopic, String jsonContent) {
        kafkaTemplate.send(inputTopic, jsonContent);
                System.out.println("\n Came in cosmos sending ");
    }


    @Override
    protected List<String> messageListen(String outputTopic) {
        try {
            
        CosmosClient client = new CosmosClientBuilder().endpoint(cosmosUri).key(cosmosKey).buildClient();
        database  = client.getDatabase(databaseName);
        container  = database.getContainer(containerName);
        System.out.println("Querying items.");
        
        CosmosPagedIterable<String> queryResults  = container.queryItems(
        "SELECT * FROM c ", new CosmosQueryRequestOptions(), String.class);

        Thread.sleep(3000);
        queryResults.forEach(processed::add); 

        } catch (Exception e) {
            System.out.println("\n Error occurred in cosmos listening" + e.getMessage());
        }
        return processed;
    }

    @Override
    public boolean messageVerify(List<String> ProcessedOutput, String ProvidedOutput) {
        try {
            Thread.sleep(9000);

            if (processed == null) {
                System.out.println("\n No Output message received");
                return false;
            } else {
                System.out.println("\n 1"+ processed );
                System.out.println("\n 2"+ ProvidedOutput);
                return processed.contains(ProvidedOutput);
            }
        } catch (Exception e) {
            System.out.println("\n Error occurred in cosmos verifying" + e.getMessage());
            return false;
        }
    }

    @Override
    protected void simulate(String outputTopic, String ProvidedOutput) {
        System.out.println("\n Came in cosmos simulate ");

        System.out.println("\n Persist ");

        CosmosClient client = new CosmosClientBuilder().endpoint(cosmosUri).key(cosmosKey).buildClient();
        try {
     
        client.createDatabaseIfNotExists(databaseName);
        database  = client.getDatabase(databaseName);

        database.createContainerIfNotExists(containerName, "/articleNumber");
        container  = database.getContainer(containerName);

        // container.createItem(ProvidedOutput);
     

        } catch ( Exception e)
        {
            System.out.println("Simulate Persisting Error."+ e);
        }

    }

}