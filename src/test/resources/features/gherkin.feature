Feature: Kafka Integration in Spring Boot Application

  Scenario: Produce a message to Kafka
    Given a Kafka topic "testTopic" exists in "localhost:9092"
    When a message "Hello, Kafka!" is sent to "testTopic"
    Then the message "Hello, Kafka!" is successfully written to "testTopic"

  Scenario: Produce and Consume  message from Kafka
    Given a Kafka topic "testTopic" exists in "localhost:9092"
    And a message "Hello, Kafka!" is present in "testTopic"
    When a consumer subscribes to "testTopic"
    Then the consumer receives the message "Hello, Kafka!"

  Scenario: Consume a message from Kafka and resend after transforming
    Given a Kafka topic "testTopic" exists in "localhost:9092"
    And a message "Hello, Kafka!" is present in "testTopic"
    When a consumer subscribes to "testTopic"
    When the consumer receives the message "Hello, Kafka!"
    When transformation logic is applied and 
    And transformed message is sent to "testTopic1"
    When a consumer subscribes to "testTopic1"
    Then the consumer receives the transformed message


 