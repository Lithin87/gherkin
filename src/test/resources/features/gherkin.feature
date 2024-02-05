Feature: Kafka Integration in Spring Boot Application


  Scenario: KafkaListener should automatically consume from a topic and send transformed msg to selected partition
    Given a MX1 sends "Inp_Dto_Scn_5.json" to "dev-buk_eapi-testsuite-input-data"
    When a consumer subscribes to "dev-buk_eapi-testsuite-output-data" from partition 2
    Then the consumer receives the message equivalent to file "Out_Dto_Scn_5_part2.json"


  Scenario: KafkaListener should automatically consume from a topic and send transformed msg to selected partition
    Given a MX1 sends "Inp_Dto_Scn_6.json" to "dev-buk_eapi-testsuite-input-data"
    When a consumer subscribes to "dev-buk_eapi-testsuite-output-data" from partition 0
    Then the consumer receives the message equivalent to file "Out_Dto_Scn_6_part0.json"


  Scenario: KafkaListener should automatically consume from a topic and send transformed msg to selected partition
    Given a MX1 sends "Inp_Dto_Scn_7.json" to "dev-buk_eapi-testsuite-input-data"
    When a consumer subscribes to "dev-buk_eapi-testsuite-output-data" from partition 1
    Then the consumer receives the message equivalent to file "Out_Dto_Scn_7_part1.json"
