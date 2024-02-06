// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.example.demo.cosmosdb;

import com.azure.cosmos.ConsistencyLevel;
import com.azure.cosmos.CosmosClient;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.CosmosDatabase;
import com.azure.cosmos.implementation.ConnectionPolicy;
import com.azure.cosmos.models.CosmosContainerProperties;
import com.azure.cosmos.models.CosmosItemRequestOptions;
import com.azure.cosmos.models.CosmosItemResponse;
import com.example.demo.model.Root;
import com.google.common.collect.Lists;

import org.springframework.stereotype.Service;

@Service
public class AzureCosmosDB {

    private CosmosClient client;

    private final String databaseName = "dbc";
    private final String containerName = "itemsc";

    private CosmosDatabase database;
    private CosmosContainer container;

    public void close() {
        client.close();
    }


    public  void persist(Root root, String args) {
      
        try {
            getStartedDemo(root, args);
            System.out.println("Demo complete, please hold while resources are released");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(String.format("Cosmos getStarted failed with %s", e));
        } finally {
            System.out.println("Closing the client");
            // this.close();
        }
       
    }


    private void getStartedDemo(Root root, String args) throws Exception {
        System.out.println("Using Azure Cosmos DB endpoint: " + AccountSettings.HOST);

        ConnectionPolicy defaultPolicy = ConnectionPolicy.getDefaultPolicy();
        defaultPolicy.setUserAgentSuffix("CosmosDBJavaQuickstart");
        //  Setting the preferred location to Cosmos DB Account region
        //  West US is just an example. User should set preferred location to the Cosmos DB region closest to the application
        defaultPolicy.setPreferredRegions(Lists.newArrayList("West US"));

        //  Create sync client
        //  <CreateSyncClient>
        client = new CosmosClientBuilder()
        .endpoint(AccountSettings.HOST)
        .key(AccountSettings.MASTER_KEY)
        .consistencyLevel(ConsistencyLevel.EVENTUAL)
        .buildClient();


        //  </CreateSyncClient>

        createDatabaseIfNotExists();
        createContainerIfNotExists();
        // scaleContainer();

        createFamilies(root, args);

        // System.out.println("Reading items.");
        // readItems(familiesToCreate);

        // System.out.println("Querying items.");
        // queryItems();
    }

    private void createDatabaseIfNotExists() throws Exception {
        System.out.println("Create database " + databaseName + " if not exists.");

        //  Create database if not exists
        //  <CreateDatabaseIfNotExists>
      client.createDatabaseIfNotExists(databaseName);
        database = client.getDatabase(databaseName);
        //  </CreateDatabaseIfNotExists>

        System.out.println("Checking database " + database.getId() + " completed!\n");
    }

    private void createContainerIfNotExists() throws Exception {
        System.out.println("Create container " + containerName + " if not exists.");

        //  Create container if not exists
        //  <CreateContainerIfNotExists>
        CosmosContainerProperties containerProperties =
            new CosmosContainerProperties(containerName, "/lastName");

        //  Create container with 400 RU/s
        // container = database.createContainerIfNotExists(containerProperties, 400).getContainer();
        //  </CreateContainerIfNotExists>

        System.out.println("Checking container " + container.getId() + " completed!\n");
    }
    
    // private void scaleContainer() throws Exception {
    //     System.out.println("Scale container " + containerName + " to 500 RU/s.");

    //     // You can scale the throughput (RU/s) of your container up and down to meet the needs of the workload. Learn more: https://aka.ms/cosmos-request-units
    //     int currentThroughput = container.readThroughput();
    //     currentThroughput = currentThroughput + 100;
    //     container.replaceProvisionedThroughput(currentThroughput);
    //     System.out.println("Scaled container to " + currentThroughput + " completed!\n");
    // }

    private void createFamilies(Root root, String args) throws Exception {
        double totalRequestCharge = 0;
 
            CosmosItemRequestOptions cosmosItemRequestOptions = new CosmosItemRequestOptions();
   
            CosmosItemResponse<Root> item = container.createItem(root);


            totalRequestCharge += item.getRequestCharge();
        
        System.out.println(String.format("Created  items with total request " +
                "charge of %.2f", totalRequestCharge));
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
