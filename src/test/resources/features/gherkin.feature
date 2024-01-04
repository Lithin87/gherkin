Feature: Kafka Integration in Spring Boot Application

  Scenario: Produce a message to Kafka
    Given a Kafka topic "testTopic" exists
    When a message "Hello, Kafka!" is sent to "testTopic"
    Then the message "Hello, Kafka!" is successfully produced to "testTopic"

  Scenario: Consume a message from Kafka
    Given a Kafka topic "testTopic" exists
    And a message "Hello, Kafka!" is present in "testTopic"
    When a consumer subscribes to "testTopic"
    Then the consumer receives the message "Hello, Kafka!"

  Scenario: Produce and consume multiple messages
    Given a Kafka topic "testTopic" exists
    When messages are sent to "testTopic":
      | Message                |
      | "Message 1"            |
      | "Message 2"            |
      | "Message 3"            |
    And a consumer subscribes to "testTopic"
    Then the consumer receives the messages:
      | Expected Message       |
      | "Message 1"            |
      | "Message 2"            |
      | "Message 3"            |

