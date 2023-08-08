@gov-223 @ops-batch-setup @ops-batch-teardown
Feature: Operations APP related feature

  Scenario: Batches API test vanilla
    Given I have tenant as "rhino"
    When I call the operations-app auth endpoint with username: "mifos" and password: "password"
    Then I should get a valid token
    When I call the batches endpoint with expected status of 200
    Then I should get non empty response
    And I am able to parse batch paginated response into DTO

  Scenario: Batches API test 2
    Given I am happy
    When I get chocolate
    Then I get more happy
