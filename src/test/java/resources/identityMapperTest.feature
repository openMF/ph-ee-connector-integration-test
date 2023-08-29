@gov
Feature: Identity Account Mapper Api Test

  Background: I will start mock server and register stub
    Given I will start the mock server
    And I can register the stub with "/registerBeneficiaryApiTest" endpoint for "PUT" request with status of 200
    And I can register the stub with "/addPaymentModalityApiTest" endpoint for "PUT" request with status of 200
    And I can register the stub with "/updatePaymentModalityApiTest" endpoint for "PUT" request with status of 200
    And I can register the stub with "/accountLookupTest" endpoint for "PUT" request with status of 200
    And I can register the stub with "/accountLookup" endpoint for "PUT" request with status of 200
    And I can register the stub with "/batchAccountLookup" endpoint for "PUT" request with status of 200
    And I can register the stub with "/updateBeneficiaryApiTest" endpoint for "PUT" request with status of 200
    Then I will update the  mock server and register stub as done


  Scenario: IAM-001 Register Beneficiary Api Test
    Given I create an IdentityMapperDTO for Register Beneficiary
    When I call the register beneficiary API with expected status of 202 and stub "/registerBeneficiaryApiTest"
    Then I will sleep for 7000 millisecond
    And I should be able to verify that the "PUT" method to "/registerBeneficiaryApiTest" endpoint received a request with required parameter in body

  Scenario: IAM-002 Add Payment Modality Api Test
    Given I create an IdentityMapperDTO for Add Payment Modality
    When I call the add payment modality API with expected status of 202 and stub "/addPaymentModalityApiTest"
    Then I will sleep for 3000 millisecond
    And I should be able to verify that the "PUT" method to "/addPaymentModalityApiTest" endpoint received a request with required parameter in body

  Scenario: IAM-003 Update Payment Modality Api Test
    Given I create an IdentityMapperDTO for Update Payment Modality
    When I call the update payment modality API with expected status of 202 and stub "/updatePaymentModalityApiTest"
    Then I will sleep for 3000 millisecond
    And I should be able to verify that the "PUT" method to "/updatePaymentModalityApiTest" endpoint received a request with required parameter in body

  Scenario: PPV-11 Account Lookup Api Test
    When I call the account lookup API with expected status of 202 and stub "/accountLookupTest"
    Then I will sleep for 3000 millisecond
    And I should be able to verify that the "PUT" method to "/accountLookupTest" endpoint received a request with same payeeIdentity

  Scenario: IAM-004 Account Lookup Api Consistency Test
    When I call the account lookup API 10 times with expected status of 202 and stub "/accountLookup"
    Then I will sleep for 3000 millisecond

  Scenario: UB-003 Update a single beneficiary with Payment Modality - Mobile Money
    When I create an IdentityMapperDTO for 1 Register Beneficiary with payment modality as "ACCOUNT_ID"
    When I call the register beneficiary API with expected status of 202 and stub "/registerBeneficiaryApiTest"
    When I create an IdentityMapperDTO for 1 update Beneficiary with payment modality as "MSISDN"
    When I call the update beneficiary API with expected status of 202 and stub "/updateBeneficiaryApiTest"
    Then I will sleep for 3000 millisecond
    Then I should be able to verify that the "PUT" method to "/updateBeneficiaryApiTest" endpoint received a request with required parameter in body

  Scenario: UB-004 Update a single beneficiary with Payment Modality - Voucher
    When I create an IdentityMapperDTO for 1 Register Beneficiary with payment modality as "MSISDN"
    When I call the register beneficiary API with expected status of 202 and stub "/registerBeneficiaryApiTest"
    When I create an IdentityMapperDTO for 1 update Beneficiary with payment modality as "VOUCHER"
    When I call the update beneficiary API with expected status of 202 and stub "/updateBeneficiaryApiTest"
    Then I will sleep for 3000 millisecond
    Then I should be able to verify that the "PUT" method to "/updateBeneficiaryApiTest" endpoint received a request with required parameter in body

  Scenario: UB-006 Update a single beneficiary with Payment Modality - Proxy or Virtual Address
    When I create an IdentityMapperDTO for 1 Register Beneficiary with payment modality as "VOUCHER"
    When I call the register beneficiary API with expected status of 202 and stub "/registerBeneficiaryApiTest"
    When I create an IdentityMapperDTO for 1 update Beneficiary with payment modality as "VIRTUAL_ADDRESS"
    When I call the update beneficiary API with expected status of 202 and stub "/updateBeneficiaryApiTest"
    Then I will sleep for 3000 millisecond
    Then I should be able to verify that the "PUT" method to "/updateBeneficiaryApiTest" endpoint received a request with required parameter in body

  Scenario: UB-005 Update a single beneficiary with Payment Modality - Digital Wallet
    When I create an IdentityMapperDTO for 1 Register Beneficiary with payment modality as "VOUCHER"
    When I call the register beneficiary API with expected status of 202 and stub "/registerBeneficiaryApiTest"
    When I create an IdentityMapperDTO for 1 update Beneficiary with payment modality as "WALLET_ID"
    When I call the update beneficiary API with expected status of 202 and stub "/updateBeneficiaryApiTest"
    Then I will sleep for 3000 millisecond
    Then I should be able to verify that the "PUT" method to "/updateBeneficiaryApiTest" endpoint received a request with required parameter in body

  Scenario: UB-009 Update  multiple beneficiary with Payment Modality - Mobile Money
    When I create an IdentityMapperDTO for 4 Register Beneficiary with payment modality as "ACCOUNT_ID"
    When I call the register beneficiary API with expected status of 202 and stub "/registerBeneficiaryApiTest"
    When I create an IdentityMapperDTO for 4 update Beneficiary with payment modality as "MSISDN"
    When I call the update beneficiary API with expected status of 202 and stub "/updateBeneficiaryApiTest"
    Then I will sleep for 3000 millisecond
    Then I should be able to verify that the "PUT" method to "/updateBeneficiaryApiTest" endpoint received a request with required parameter in body

  Scenario: UB-010 Update  multiple beneficiary with Payment Modality - Voucher
    When I create an IdentityMapperDTO for 4 Register Beneficiary with payment modality as "MSISDN"
    When I call the register beneficiary API with expected status of 202 and stub "/registerBeneficiaryApiTest"
    When I create an IdentityMapperDTO for 4 update Beneficiary with payment modality as "VOUCHER"
    When I call the update beneficiary API with expected status of 202 and stub "/updateBeneficiaryApiTest"
    Then I will sleep for 3000 millisecond
    Then I should be able to verify that the "PUT" method to "/updateBeneficiaryApiTest" endpoint received a request with required parameter in body

  Scenario: UB-011 Update  multiple beneficiary with Payment Modality - Digital Wallet
    When I create an IdentityMapperDTO for 4 Register Beneficiary with payment modality as "VOUCHER"
    When I call the register beneficiary API with expected status of 202 and stub "/registerBeneficiaryApiTest"
    When I create an IdentityMapperDTO for 4 update Beneficiary with payment modality as "WALLET_ID"
    When I call the update beneficiary API with expected status of 202 and stub "/updateBeneficiaryApiTest"
    Then I will sleep for 3000 millisecond
    Then I should be able to verify that the "PUT" method to "/updateBeneficiaryApiTest" endpoint received a request with required parameter in body

  Scenario: UB-012 Update  multiple beneficiary with Payment Modality - Proxy or Virtual Address
    When I create an IdentityMapperDTO for 4 Register Beneficiary with payment modality as "WALLET_ID"
    When I call the register beneficiary API with expected status of 202 and stub "/registerBeneficiaryApiTest"
    When I create an IdentityMapperDTO for 4 update Beneficiary with payment modality as "VIRTUAL_ADDRESS"
    When I call the update beneficiary API with expected status of 202 and stub "/updateBeneficiaryApiTest"
    Then I will sleep for 3000 millisecond
    Then I should be able to verify that the "PUT" method to "/updateBeneficiaryApiTest" endpoint received a request with required parameter in body

  Scenario: Update a single beneficiary with Banking Institution Code
    When I create an IdentityMapperDTO for 1 Register Beneficiary with payment modality as "ACCOUNT_ID"
    When I call the register beneficiary API with expected status of 202 and stub "/registerBeneficiaryApiTest"
    When I create an IdentityMapperDTO for 1 update Beneficiary with banking institution code as "lion"
    When I call the update beneficiary API with expected status of 202 and stub "/updateBeneficiaryApiTest"
    Then I will sleep for 3000 millisecond
    Then I should be able to verify that the "PUT" method to "/updateBeneficiaryApiTest" endpoint received a request with required parameter in body

  Scenario: UB-002 Update a single beneficiary with Financial Address
    When I create an IdentityMapperDTO for 1 Register Beneficiary with payment modality as "ACCOUNT_ID"
    When I call the register beneficiary API with expected status of 202 and stub "/registerBeneficiaryApiTest"
    When I create an IdentityMapperDTO for 1 update Beneficiary with financial address code as "123456789"
    When I call the update beneficiary API with expected status of 202 and stub "/updateBeneficiaryApiTest"
    Then I will sleep for 3000 millisecond
    Then I should be able to verify that the "PUT" method to "/updateBeneficiaryApiTest" endpoint received a request with required parameter in body

  Scenario: UB-008 Update a multiple beneficiary with Financial Address
    When I create an IdentityMapperDTO for 4 Register Beneficiary with payment modality as "ACCOUNT_ID"
    When I call the register beneficiary API with expected status of 202 and stub "/registerBeneficiaryApiTest"
    When I create an IdentityMapperDTO for 4 update Beneficiary with financial address code as "123456789"
    When I call the update beneficiary API with expected status of 202 and stub "/updateBeneficiaryApiTest"
    Then I will sleep for 3000 millisecond
    Then I should be able to verify that the "PUT" method to "/updateBeneficiaryApiTest" endpoint received a request with required parameter in body

  Scenario: RB-001 Register a single beneficiary with Payment Modality - Bank Account
    When I create an IdentityMapperDTO for 1 Register Beneficiary with payment modality as "ACCOUNT_ID"
    When I call the register beneficiary API with expected status of 202 and stub "/registerBeneficiaryApiTest"
    Then I will sleep for 3000 millisecond
    Then I should be able to verify that the "PUT" method to "/registerBeneficiaryApiTest" endpoint received a request with required parameter in body

  Scenario: RB-002 Register a single beneficiary with Payment Modality - Mobile Money Account
    When I create an IdentityMapperDTO for 1 Register Beneficiary with payment modality as "MSISDN"
    When I call the register beneficiary API with expected status of 202 and stub "/registerBeneficiaryApiTest"
    Then I will sleep for 3000 millisecond
    Then I should be able to verify that the "PUT" method to "/registerBeneficiaryApiTest" endpoint received a request with required parameter in body

  Scenario: RB-003 Register a single beneficiary with Payment Modality - Proxy or Virtual Address
    When I create an IdentityMapperDTO for 1 Register Beneficiary with payment modality as "VIRTUAL_ADDRESS"
    When I call the register beneficiary API with expected status of 202 and stub "/registerBeneficiaryApiTest"
    Then I will sleep for 3000 millisecond
    Then I should be able to verify that the "PUT" method to "/registerBeneficiaryApiTest" endpoint received a request with required parameter in body

  Scenario: RB-004 Register a single beneficiary with Payment Modality - Digital Wallet ID
    When I create an IdentityMapperDTO for 1 Register Beneficiary with payment modality as "WALLET_ID"
    When I call the register beneficiary API with expected status of 202 and stub "/registerBeneficiaryApiTest"
    Then I will sleep for 3000 millisecond
    Then I should be able to verify that the "PUT" method to "/registerBeneficiaryApiTest" endpoint received a request with required parameter in body

  Scenario: RB-005 Register a single beneficiary with Payment Modality - Voucher
    When I create an IdentityMapperDTO for 1 Register Beneficiary with payment modality as "VOUCHER"
    When I call the register beneficiary API with expected status of 202 and stub "/registerBeneficiaryApiTest"
    Then I will sleep for 3000 millisecond
    Then I should be able to verify that the "PUT" method to "/registerBeneficiaryApiTest" endpoint received a request with required parameter in body

  Scenario: RB-006 Register multiple beneficiary with Payment Modality - Bank Account
    When I create an IdentityMapperDTO for 5 Register Beneficiary with payment modality as "ACCOUNT_ID"
    When I call the register beneficiary API with expected status of 202 and stub "/registerBeneficiaryApiTest"
    Then I will sleep for 3000 millisecond
    Then I should be able to verify that the "PUT" method to "/registerBeneficiaryApiTest" endpoint received a request with required parameter in body

  Scenario: RB-007 Register multiple beneficiary with Payment Modality - Mobile Money Account
    When I create an IdentityMapperDTO for 5 Register Beneficiary with payment modality as "MSISDN"
    When I call the register beneficiary API with expected status of 202 and stub "/registerBeneficiaryApiTest"
    Then I will sleep for 3000 millisecond
    Then I should be able to verify that the "PUT" method to "/registerBeneficiaryApiTest" endpoint received a request with required parameter in body

  Scenario: RB-008 Register multiple beneficiary with Payment Modality - Proxy or Virtual Address
    When I create an IdentityMapperDTO for 5 Register Beneficiary with payment modality as "VIRTUAL_ADDRESS"
    When I call the register beneficiary API with expected status of 202 and stub "/registerBeneficiaryApiTest"
    Then I will sleep for 3000 millisecond
    Then I should be able to verify that the "PUT" method to "/registerBeneficiaryApiTest" endpoint received a request with required parameter in body

  Scenario: RB-009 Register multiple beneficiary with Payment Modality - Digital Wallet ID
    When I create an IdentityMapperDTO for 5 Register Beneficiary with payment modality as "WALLET_ID"
    When I call the register beneficiary API with expected status of 202 and stub "/registerBeneficiaryApiTest"
    Then I will sleep for 3000 millisecond
    Then I should be able to verify that the "PUT" method to "/registerBeneficiaryApiTest" endpoint received a request with required parameter in body

  Scenario: RB-010 Register multiple beneficiary with Payment Modality - Voucher
    When I create an IdentityMapperDTO for 5 Register Beneficiary with payment modality as "VOUCHER"
    When I call the register beneficiary API with expected status of 202 and stub "/registerBeneficiaryApiTest"
    Then I will sleep for 3000 millisecond
    Then I should be able to verify that the "PUT" method to "/registerBeneficiaryApiTest" endpoint received a request with required parameter in body

  Scenario: PPV-04 Invalid Registering Institution Id
    When I create an IdentityMapperDTO for adding 8 beneficiary
    When I call the register beneficiary API with "SocialWelfare" as registering institution id expected status of 202 and stub "/registerBeneficiaryApiTest"
    And I create an IdentityMapperDTO for adding 2 beneficiary
    When I call the register beneficiary API with "Social" as registering institution id expected status of 202 and stub "/registerBeneficiaryApiTest"
    And I create request body for batch account lookup API
    Then I call bulk account lookup API with these 10 beneficiaries and "Social" as registering institution id and stub "/batchAccountLookup"
    Then I will sleep for 3000 millisecond
    And I should be able to verify that the "PUT" method to "/batchAccountLookup" receive 2 request

  Scenario: PPV-02 Beneficiary not registered
    When I create an IdentityMapperDTO for adding 8 beneficiary
    When I call the register beneficiary API with "SocialWelfare" as registering institution id expected status of 202 and stub "/registerBeneficiaryApiTest"
    And I create an IdentityMapperDTO for adding 2 beneficiary
    And I create request body for batch account lookup API
    Then I call bulk account lookup API with these 10 beneficiaries and "SocialWelfare" as registering institution id and stub "/batchAccountLookup"
    Then I will sleep for 3000 millisecond
    And I should be able to verify that the "PUT" method to "/batchAccountLookup" receive 8 request

  Scenario: PPV-02 Batch Account Lookup API
    When I create an IdentityMapperDTO for adding 8 beneficiary
    When I call the register beneficiary API with "SocialWelfare" as registering institution id expected status of 202 and stub "/registerBeneficiaryApiTest"
    And I create request body for batch account lookup API
    Then I call bulk account lookup API with these 8 beneficiaries and "SocialWelfare" as registering institution id and stub "/batchAccountLookup"
    Then I will sleep for 3000 millisecond
    And I should be able to verify that the "PUT" method to "/batchAccountLookup" receive 8 request
    And I can stop mock server