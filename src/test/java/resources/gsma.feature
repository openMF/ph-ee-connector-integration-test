Feature: GSMA Transfer API Test for Account Identifier Worker

  Scenario: Savings account Creation Test
    Given I have Fineract-Platform-TenantId as "gorilla"
    When I call the create payer client endpoint
    Then I call the create savings product endpoint
    When I call the create savings account endpoint
    Then I approve the deposit with command "approve"
    When I activate the account with command "activate"
    Then I call the deposit account endpoint for amount 11

  Scenario: Loan account Creation Test
    Given I have Fineract-Platform-TenantId as "gorilla"
    And I call the create loan product endpoint
    When I call the create loan account
    Then I approve the loan account with command "approve" for amount 7800
    When I call the loan disburse endpoint with command "disburse" for amount 7800
    Then I call the loan repayment endpoint for amount 21

  Scenario: Deposit Savings Worker Test
    Given I have accountId "S7741025618"
    When I have amsName as "mifos" and acccountHoldingInstitutionId as "gorilla" and amount as 11
    Then I call the channel connector API with expected status of 200

  Scenario: Loan repayment Worker Test
    Given I have accountId "L000000003"
    When I have amsName as "mifos" and acccountHoldingInstitutionId as "gorilla" and amount as 11
    Then I call the channel connector API with expected status of 200