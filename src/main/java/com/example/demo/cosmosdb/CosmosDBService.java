// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.example.demo.cosmosdb;

import com.azure.cosmos.*;
import com.example.demo.model.Root;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.azure.cosmos.models.*;
import com.azure.cosmos.util.CosmosPagedIterable;


@Service
public class CosmosDBService {

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
    private final ObjectMapper objectMapper = null;

    public  void persist(Root root) {
        CosmosClient client = new CosmosClientBuilder().endpoint(cosmosUri).key(cosmosKey).buildClient();
        try {
     
        client.createDatabaseIfNotExists(databaseName);
        database  = client.getDatabase(databaseName);

        database.createContainerIfNotExists(containerName, "/articleNumber");
        container  = database.getContainer(containerName);

        container.createItem(root);
     
        // System.out.println("Reading items.");
        // readItems(familiesToCreate);

        } catch ( Exception e)
        {
            System.out.println("Querying Error."+ e);
        }
    }


    public boolean verify(String container2, String fileContent) throws JsonMappingException, JsonProcessingException {

        CosmosClient client = new CosmosClientBuilder().endpoint(cosmosUri).key(cosmosKey).buildClient();
        database  = client.getDatabase(databaseName);
        container  = database.getContainer(containerName);
        System.out.println("Querying items.");
        
        CosmosPagedIterable<Root> queryResults  = container.queryItems(
        "SELECT * FROM c  WHERE c.articleNumber = 'A12345' ", new CosmosQueryRequestOptions(), Root.class);

        List<Root> dbRecords = new ArrayList<>();
        queryResults.forEach(dbRecords::add); 

        Root root = objectMapper.readValue(fileContent, Root.class);

    for (Root dbRecord : dbRecords) {

        if (dbRecord.equals(root)) {
            return true;
        }
    }
            return false;
    }




    // private void readItems(ArrayList<Family> familiesToCreate) {
    //     //  Using partition key for point read scenarios.
    //     //  This will help fast look up of items because of partition key
    //     familiesToCreate.forEach(family -> {
    //         //  <ReadItem>

    //         CosmosItem item = container.getItem(family.getId(), family.getLastName());
    //         try {
    //             CosmosItemResponse read = item.read(new CosmosItemRequestOptions(family.getLastName()));
    //             double requestCharge = read.getRequestCharge();
    //             Duration requestLatency = read.getRequestLatency();
    //             System.out.println(String.format("Item successfully read with id %s with a charge of %.2f and within duration %s",
    //                 read.getItem().getId(), requestCharge, requestLatency));
    //         } catch (CosmosClientException e) {
    //             e.printStackTrace();
    //             System.err.println(String.format("Read Item failed with %s", e));
    //         }
    //         //  </ReadItem>
    //     });
    // }

    // private void queryItems() {



    //     FeedOptions queryOptions = new FeedOptions();
    //     queryOptions.maxItemCount(10);
    //     queryOptions.setEnableCrossPartitionQuery(true);
    //     //  Set populate query metrics to get metrics around query executions
    //     queryOptions.populateQueryMetrics(true);

    //     Iterator<FeedResponse<CosmosItemProperties>> feedResponseIterator = container.queryItems(
    //         "SELECT * FROM Family WHERE Family.lastName IN ('Andersen', 'Wakefield', 'Johnson')", queryOptions);

    //     feedResponseIterator.forEachRemaining(cosmosItemPropertiesFeedResponse -> {
    //         System.out.println("Got a page of query result with " +
    //             cosmosItemPropertiesFeedResponse.getResults().size() + " items(s)"
    //             + " and request charge of " + cosmosItemPropertiesFeedResponse.getRequestCharge());

    //         System.out.println("Item Ids " + cosmosItemPropertiesFeedResponse
    //             .getResults()
    //             .stream()
    //             .map(Resource::getId)
    //             .collect(Collectors.toList()));
    //     });

    // }
}
