Feature: Kafka Integration in Spring Boot Application

  Scenario: Produce a message to Kafka
    Given a Kafka topic "testTopic" exists in "localhost:9092"
    When a message "Hello, Kafka!" is sent to "testTopic"
    Then the message "Hello, Kafka!" is successfully written to "testTopic"

  Scenario: Produce and Consume  message from Kafka
    Given a Kafka topic "testTopic" exists in "localhost:9092"
    And a message "Hello, Kafka!" is present in "testTopic"
    When a consumer subscribes to "testTopic"
    Then the consumer receives the message "Hello, Kafka!" from "testTopic"

 

 