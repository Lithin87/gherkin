Feature: Kafka Integration in Spring Boot Application



  Scenario: Produce a message to Kafka
    Given a Kafka topic "testTopic" exists in "localhost:9092"
    When a message "testKafkaConsumer" is sent to "testTopic"
    Then the message "testKafkaConsumer" is successfully written to "testTopic"

  Scenario: Consume a message from Kafka
    Given a Kafka topic "testTopic" exists in "localhost:9092"
    And a message "testKafkaConsumer" is present in "testTopic"
    When a consumer subscribes to "testTopic"
    Then the consumer receives the message "testKafkaConsumer" from "testTopic"

  Scenario: Consume from a File
    When a file "Inp_File_1.txt" is sent to "testFileTopic"
    When json is retrieved from "testFileTopic" and transformed and sent to "testFileTopicProcessed"
    When a consumer subscribes to "testFileTopicProcessed"
    Then the consumer receives the message equivalent to file "Out_File_1.txt"