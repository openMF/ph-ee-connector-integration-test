@govtodo
Feature: Validation Error Code Test

  Scenario: GSMA Transfer Api NegativeValue Test
    Given I can create GSMATransferDTO with Negative Amount
    And I have tenant as "gorilla"
    When I call the GSMATransfer endpoint with expected status of 400
    And I should be able to parse "NegativeValue" Error Code from GSMA Transfer response

  Scenario: GSMA Transfer Api MandatoryValueNotSupplied Test
    Given I can create GSMATransferDTO with missing currency details
    And I have tenant as "gorilla"
    When I call the GSMATransfer endpoint with expected status of 400
    And I should be able to parse "MandatoryValueNotSupplied" Error Code from GSMA Transfer response

  Scenario: GSMA Transfer Api SamePartiesError Test
    Given I can create GSMATransferDTO with same payer and payee
    And I have tenant as "gorilla"
    When I call the GSMATransfer endpoint with expected status of 400
    And I should be able to parse "SamePartiesError" Error Code from GSMA Transfer response

  Scenario: GSMA Transfer Api FormatError Test
    Given I can create GSMATransferDTO with invalid amount format
    And I have tenant as "gorilla"
    When I call the GSMATransfer endpoint with expected status of 400
    And I should be able to parse "FormatError" Error Code from GSMA Transfer response



