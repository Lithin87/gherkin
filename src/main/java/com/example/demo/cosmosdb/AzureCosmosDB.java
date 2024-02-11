// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.example.demo.cosmosdb;

import com.azure.cosmos.*;
import com.example.demo.model.Root;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AzureCosmosDB {

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


    public  void persist(Root root, String args) {

         try (CosmosClient client = new CosmosClientBuilder().endpoint(cosmosUri).key(cosmosKey).buildClient()) {
      

        // ConnectionPolicy defaultPolicy = ConnectionPolicy.getDefaultPolicy();
        // defaultPolicy.setUserAgentSuffix("CosmosDBJavaQuickstart");
        // defaultPolicy.setPreferredRegions(Lists.newArrayList("West US"));

     
    
        client.createDatabaseIfNotExists(databaseName);
        database  = client.getDatabase(databaseName);

        database.createContainerIfNotExists(containerName, "/articleNumber");
        container  = database.getContainer(containerName);

        // scaleContainer();


        container.createItem(root);
        System.out.println("Created  items with total request ");


        // System.out.println("Reading items.");
        // readItems(familiesToCreate);

        // System.out.println("Querying items.");
        // queryItems();
        } 
    }


    
    // private void scaleContainer() throws Exception {
    //     System.out.println("Scale container " + containerName + " to 500 RU/s.");

    //     // You can scale the throughput (RU/s) of your container up and down to meet the needs of the workload. Learn more: https://aka.ms/cosmos-request-units
    //     int currentThroughput = container.readThroughput();
    //     currentThroughput = currentThroughput + 100;
    //     container.replaceProvisionedThroughput(currentThroughput);
    //     System.out.println("Scaled container to " + currentThroughput + " completed!\n");
    // }


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
    //     //  <QueryItems>
    //     // Set some common query options
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
    //     //  </QueryItems>
    // }
}
