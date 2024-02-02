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


  Scenario: Consume from a topic and send transformed message and verify 
    When a MX1 sends "Inp_Scn_3.json" to "wholeFileTopic"
    When json is retrieved from "wholeFileTopic" and transformed and sent to "wholeFileTopicProcessed"
    When a consumer subscribes to "wholeFileTopicProcessed"
    Then the consumer receives the message equivalent to file "Out_Scn_3.json"


  Scenario: Consume from a topic and send selectively transformed message and verify 
    Given a MX1 sends "Inp_Scn_3.json" to "selectiveFileTopic"
    When MX2 retrieves from "selectiveFileTopic" and does selective transformation with below data and sends to "selectiveFileTopicProcessed":
      | eventType   | INSERT |
      | company  | allianz |
      | account  | anthem |
    When a consumer subscribes to "selectiveFileTopicProcessed"
    Then the consumer receives the message equivalent to file "Out_Selective_Scn_4.json"


  Scenario: KafkaListener should automatically consume from a topic and send transformed msg to selected partition
    Given a MX1 sends "Inp_Dto_Scn_5.json" to "dev-buk_eapi-testsuite-input-data"
    When a consumer subscribes to "dev-buk_eapi-testsuite-output-data" to partition 2
    Then the consumer receives the message equivalent to file "Out_Dto_Scn_5.json"


  Scenario: KafkaListener should automatically consume from a topic and send transformed msg to selected partition
    Given a MX1 sends "Inp_Dto_Scn_6.json" to "dev-buk_eapi-testsuite-input-data"
    When a consumer subscribes to "dev-buk_eapi-testsuite-output-data" to partition 1
    Then the consumer receives the message equivalent to file "Out_Dto_Scn_5.json"


  Scenario: KafkaListener should automatically consume from a topic and send transformed msg to selected partition
    Given a MX1 sends "Inp_Dto_Scn_7.json" to "dev-buk_eapi-testsuite-input-data"
    When a consumer subscribes to "dev-buk_eapi-testsuite-output-data" to partition 0
    Then the consumer receives the message equivalent to file "Out_Dto_Scn_5.json"
