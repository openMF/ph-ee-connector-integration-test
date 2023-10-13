Feature: Batch Details API test

  Background: I will start mock server and register stub
    Given I will start the mock server
    And I can register the stub with "/authorization/callback" endpoint for "PUT" request with status of 200
    Then I will update the  mock server and register stub as done

  @gov @batch-teardown
  Scenario: BD-001 Batch transactions API Test
    Given I have the demo csv file "ph-ee-bulk-demo-6.csv"
    And I have tenant as "gorilla"
    And I have the demo csv file "ph-ee-bulk-demo-6.csv"
    And I generate clientCorrelationId
    And I have private key
    And I generate signature
    When I call the batch transactions endpoint with expected status of 202
    Then I should get non empty response

  @gov @batch-teardown
  Scenario: BD-002 Batch transactions API Test with polling callback url
    Given I have the demo csv file "ph-ee-bulk-demo-6.csv"
    And I have tenant as "gorilla"
    And I generate clientCorrelationId
    And I have private key
    And I generate signature
    When I call the batch transactions endpoint with expected status of 202
    Then I should get non empty response
    And I should have "PollingPath" and "SuggestedCallbackSeconds" in response

  @gov @batch-teardown
  Scenario: BD-003 Batch summary API Test
    Given I have a batch id from previous scenario
    And I have tenant as "gorilla"
    And I have the demo csv file "ph-ee-bulk-demo-6.csv"
    And I generate clientCorrelationId
    And I have private key
    And I generate signature
    When I call the batch transactions endpoint with expected status of 202
    Then I should get non empty response
    And I am able to parse batch transactions response
    And I fetch batch ID from batch transaction API's response
    Then I will sleep for 5000 millisecond
    When I call the operations-app auth endpoint with username: "mifos" and password: "password"
    Then I should get a valid token
    When I call the batch summary API with expected status of 200
    Then I should get non empty response

  @gov @batch-teardown
  Scenario: BD-004 Batch Details API Test
    Given I have a batch id from previous scenario
    And I have tenant as "gorilla"
    And I have the demo csv file "ph-ee-bulk-demo-6.csv"
    And I generate clientCorrelationId
    And I have private key
    And I generate signature
    When I call the batch transactions endpoint with expected status of 202
    Then I should get non empty response
    And I am able to parse batch transactions response
    And I fetch batch ID from batch transaction API's response
    When I call the operations-app auth endpoint with username: "mifos" and password: "password"
    Then I should get a valid token
    When I call the batch details API with expected status of 200
    Then I should get non empty response


  	@gov @batch-teardown
    Scenario: BD-005 Batch transaction API Test for Synchronous File Validation with empty file
      Given I have tenant as "gorilla"
      And I make sure there is no file
      And I generate clientCorrelationId
      And I have private key
      And I generate signature
      When I call the batch transactions endpoint with expected status of 400 without payload
      Then I should get non empty response
      And I should have "errorInformation" and "File not uploaded" in response

  @gov @batch-teardown
  Scenario: BD-006 Batch transaction API Test for Synchronous File Validation with invalid file
    Given I have tenant as "gorilla"
    And I have the demo csv file "ph-ee-bulk-demoErrorSync-6.csv"
    And I generate clientCorrelationId
    And I have private key
    And I generate signature
    When I call the batch transactions endpoint with expected status of 400
    Then I should get non empty response
    And I should have "Error Information" and "Invalid file structure" in response

  @gov @batch-teardown
  Scenario: BD-007 Batch transaction API Test for Asynchronous File Validation
    Given I have tenant as "gorilla"
    And I have the demo csv file "ph-ee-bulk-demoErrorAsync-6.csv"
    And I generate clientCorrelationId
    And I have private key
    And I generate signature
    When I call the batch transactions endpoint with expected status of 202
    Then I should get non empty response
    And I am able to parse batch transactions response
    And I fetch batch ID from batch transaction API's response
    Then I will sleep for 5000 millisecond
    And I have tenant as "gorilla"
    When I call the operations-app auth endpoint with username: "mifos" and password: "password"
    Then I should get a valid token
    When I call the batch summary API with expected status of 200
    Then I should get non empty response

  @gov @batch-teardown
  Scenario: BD-008 Batch Phased Callback API Test Success
    Given I have a batch id from previous scenario
    And I have tenant as "gorilla"
    And I have callbackUrl as simulated url
    And I have the demo csv file "ph-ee-bulk-demo-6.csv"
    And I generate clientCorrelationId
    And I have private key
    And I generate signature
    When I call the batch transactions endpoint with expected status of 202
    Then I should get non empty response
    And I have retry count as 2
    When I should call callbackUrl api
    Then I should get expected status of 200

  @govtodo @batch-teardown
  Scenario: BD-009 Batch Phased Callback API Test Failure
    Given I have a batch id from previous scenario
    And I have tenant as "gorilla"
    And I have the demo csv file "ph-ee-bulk-demo-6.csv"
    And I generate clientCorrelationId
    And I have private key
    And I generate signature
    And I have callbackUrl as "http://httpstat.us/503"
    When I call the batch transactions endpoint with expected status of 202
    Then I should get non empty response
    Given I have retry count as 2
    When I should call callbackUrl api
    Then I should get expected status of 503

  @gov @batch-teardown
  Scenario: BD-010 Batch summary with failure percent API Test
    Given I have a batch id from previous scenario
    And I have tenant as "gorilla"
    And I have the demo csv file "ph-ee-bulk-demo-6.csv"
    And I generate clientCorrelationId
    And I have private key
    And I generate signature
    When I call the batch transactions endpoint with expected status of 202
    Then I should get non empty response
    When I call the operations-app auth endpoint with username: "mifos" and password: "password"
    Then I should get a valid token
    When I call the batch details API with expected status of 200
    Then I should get non empty response with failure and success percentage

<<<<<<< HEAD

  @gov @batch-teardown
  Scenario: BD-011 Batch test for payerIdentifier resolution using budgetAccount info
    Given I have tenant as "rhino"
	And I have the demo csv file "payerIdentifier-resolution-using-budgetAccount.csv"
    And I have the registeringInstituteId "123"
    And I have the programId "SocialWelfare"
    And I generate clientCorrelationId
    And I have private key
    And I generate signature
    When I call the batch transactions endpoint with expected status of 202
    And I am able to parse batch transactions response
    And I fetch batch ID from batch transaction API's response
    Then I will sleep for 5000 millisecond
    When I call the batch summary API with expected status of 200
    Then I am able to parse batch summary response
    And Status of transaction is "COMPLETED"

  @gov @batch-teardown
  Scenario: BD-012 Batch Transaction REST Api test
    Given I have tenant as "rhino"
    And I create a new clientCorrelationId
	And I can mock the Batch Transaction Request DTO without payer info
    And I have private key
    And I generate signature
    When I call the batch transactions raw endpoint with expected status of 202
    And I am able to parse batch transactions response
    And I fetch batch ID from batch transaction API's response
    Then I will sleep for 5000 millisecond
    When I call the batch summary API with expected status of 200
    Then I am able to parse batch summary response
    And Status of transaction is "COMPLETED"
    And I should have matching total txn count and successful txn count in response

  @gov @batch-teardown
  Scenario: BD-013 Batch aggregate API Test
    Given I have tenant as "rhino"
    And I have the demo csv file "ph-ee-bulk-demo-7.csv"
    And I create a new clientCorrelationId
    And I have private key
    And I generate signature
    When I call the batch transactions endpoint with expected status of 202
    Then I should get non empty response
    And I am able to parse batch transactions response
    And I fetch batch ID from batch transaction API's response
    Then I will sleep for 5000 millisecond
    When I call the batch aggregate API with expected status of 200
    Then I should get non empty response
    Then I am able to parse batch summary response
    And Status of transaction is "COMPLETED"
    And I should have matching total txn count and successful txn count in response

  @gov
  Scenario: BA-001 Batch Authorization API test
    When I create an AuthorizationRequest for Batch Authorization with batch ID as "1234", payerIdentifier as "5678", currency as "USD" and amount as "30"
    And I call the Authorization API with batchId as "1234" and expected status of 202 and stub "/authorization/callback"
    And I will sleep for 7000 millisecond
    Then I should be able to verify that the "POST" method to "/authorization/callback" endpoint received a request with authorization status
