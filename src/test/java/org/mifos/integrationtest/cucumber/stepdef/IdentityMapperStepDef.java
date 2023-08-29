package org.mifos.integrationtest.cucumber.stepdef;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.google.common.truth.Truth.assertThat;
import static org.mifos.integrationtest.common.HttpMethod.PUT;

import com.github.tomakehurst.wiremock.stubbing.ServeEvent;
import io.cucumber.core.internal.com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.core.internal.com.fasterxml.jackson.databind.JsonNode;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import java.io.File;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mifos.connector.common.channel.dto.TransactionChannelRequestDTO;
import org.mifos.connector.common.identityaccountmapper.dto.AccountMapperRequestDTO;
import org.mifos.connector.common.identityaccountmapper.dto.BeneficiaryDTO;
import org.mifos.integrationtest.common.Utils;

public class IdentityMapperStepDef extends BaseStepDef {

    private static String identityMapperBody = null;
    private static AccountMapperRequestDTO registerBeneficiaryBody = null;
    private static AccountMapperRequestDTO addPaymentModalityBody = null;
    private static AccountMapperRequestDTO updatePaymentModalityBody = null;
    private static String payeeIdentity;
    private static int getApiCount = 0;
    private static String fieldName = "numberFailedCases";
    private static String fieldValue = "0";
    private static String requestId;
    private static final String payeeIdentityAccountLookup = "003001003873110196";
    private static TransactionChannelRequestDTO transactionChannelRequestDTO = new TransactionChannelRequestDTO();
    private static String transactionId;
    private static String tenant;
    private static String payeeDfspId;
    private static String sourceBBID = "SocialWelfare";
    private static List<BeneficiaryDTO> beneficiaryList = new ArrayList<>();
    private static AccountMapperRequestDTO batchAccountLookupBody = null;
    private static String callbackBody;
    private static Map<String, String> paymentModalityList = new HashMap<String, String>() {
        {
            put("ACCOUNT_ID", "00");
            put("MSISDN", "01");
            put("VIRTUAL_ADDRESS", "02");
            put("WALLET_ID", "03");
            put("VOUCHER", "04");
        }
    };


    @When("I call the register beneficiary API with expected status of {int} and stub {string}")
    public void iCallTheRegisterBeneficiaryAPIWithExpectedStatusOf(int expectedStatus, String stub) {
        RequestSpecification requestSpec = Utils.getDefaultSpec();
        BaseStepDef.response = RestAssured.given(requestSpec).header("Content-Type", "application/json").header("X-Registering-Institution-ID", sourceBBID)
                .header("X-CallbackURL", identityMapperConfig.callbackURL + stub).baseUri(identityMapperConfig.identityMapperContactPoint)
                .body(registerBeneficiaryBody).expect().spec(new ResponseSpecBuilder().expectStatusCode(expectedStatus).build()).when()
                .post(identityMapperConfig.registerBeneficiaryEndpoint).andReturn().asString();


        logger.info("Identity Mapper Response: {}", BaseStepDef.response);
    }

    @When("I call the add payment modality API with expected status of {int} and stub {string}")
    public void iCallTheAddPaymentModalityAPIWithExpectedStatusOf(int expectedStatus, String stub) {
        RequestSpecification requestSpec = Utils.getDefaultSpec();
        BaseStepDef.response = RestAssured.given(requestSpec).header("Content-Type", "application/json").header("X-Registering-Institution-ID", sourceBBID)
                .header("X-CallbackURL", identityMapperConfig.callbackURL + stub).baseUri(identityMapperConfig.identityMapperContactPoint)
                .body(addPaymentModalityBody).expect().spec(new ResponseSpecBuilder().expectStatusCode(expectedStatus).build()).when()
                .post(identityMapperConfig.addPaymentModalityEndpoint).andReturn().asString();

        logger.info("Identity Mapper Response: {}", BaseStepDef.response);
    }

    @When("I call the update payment modality API with expected status of {int} and stub {string}")
    public void iCallTheUpdatePaymentModalityAPIWithExpectedStatusOf(int expectedStatus, String stub) {
        RequestSpecification requestSpec = Utils.getDefaultSpec();
        BaseStepDef.response = RestAssured.given(requestSpec).header("Content-Type", "application/json").header("X-Registering-Institution-ID", sourceBBID)
                .header("X-CallbackURL", identityMapperConfig.callbackURL + stub).baseUri(identityMapperConfig.identityMapperContactPoint)
                .body(updatePaymentModalityBody).expect().spec(new ResponseSpecBuilder().expectStatusCode(expectedStatus).build()).when()
                .put(identityMapperConfig.updatePaymentModalityEndpoint).andReturn().asString();

        logger.info("Identity Mapper Response: {}", BaseStepDef.response);
    }

    @And("I create an IdentityMapperDTO for Register Beneficiary")
    public void iCreateAnIdentityMapperDTOForRegisterBeneficiary() {
        List<BeneficiaryDTO> beneficiaryDTOList = new ArrayList<>();
        payeeIdentity = generateUniqueNumber(16);
        BeneficiaryDTO beneficiaryDTO = new BeneficiaryDTO(payeeIdentity, null, null, null);
        beneficiaryDTOList.add(beneficiaryDTO);
        requestId = generateUniqueNumber(12);
        registerBeneficiaryBody = new AccountMapperRequestDTO(requestId, sourceBBID, beneficiaryDTOList);

    }

    @And("I create an IdentityMapperDTO for Add Payment Modality")
    public void iCreateAnIdentityMapperDTOForAddPaymentModality() {
        List<BeneficiaryDTO> beneficiaryDTOList = new ArrayList<>();

        BeneficiaryDTO beneficiaryDTO = new BeneficiaryDTO(payeeIdentity, "00", "12345678", null);
        beneficiaryDTOList.add(beneficiaryDTO);
        requestId = generateUniqueNumber(18);
        addPaymentModalityBody = new AccountMapperRequestDTO(requestId, sourceBBID, beneficiaryDTOList);

    }

    @And("I create an IdentityMapperDTO for Update Payment Modality")
    public void iCreateAnIdentityMapperDTOForUpdatePaymentModality() {
        List<BeneficiaryDTO> beneficiaryDTOList = new ArrayList<>();

        BeneficiaryDTO beneficiaryDTO = new BeneficiaryDTO(payeeIdentity, "00", "LB28369763644714781256435714", "test");
        beneficiaryDTOList.add(beneficiaryDTO);
        requestId = generateUniqueNumber(10);
        updatePaymentModalityBody = new AccountMapperRequestDTO(requestId, sourceBBID, beneficiaryDTOList);
    }

    @Then("I call the account lookup API with expected status of {int} and stub {string}")
    public void iCallTheAccountLookupAPIWithExpectedStatusOf(int expectedStatus, String stub) {
        RequestSpecification requestSpec = Utils.getDefaultSpec();
        BaseStepDef.response = RestAssured.given(requestSpec).header("Content-Type", "application/json").header("X-Registering-Institution-ID", sourceBBID)
                .header("X-CallbackURL", identityMapperConfig.callbackURL + stub).queryParam("payeeIdentity", payeeIdentity)
                .queryParam("paymentModality", "00").queryParam("requestId", generateUniqueNumber(10))
                .baseUri(identityMapperConfig.identityMapperContactPoint).expect()
                .spec(new ResponseSpecBuilder().expectStatusCode(expectedStatus).build()).when()
                .get(identityMapperConfig.accountLookupEndpoint).andReturn().asString();

        logger.info("Identity Mapper Response: {}", BaseStepDef.response);
    }

    @Then("I call the account lookup API {int} times with expected status of {int} and stub {string}")
    public void iCallTheAccountLookupAPITimesWithExpectedStatusOf(int count, int expectedStatus, String endpoint) {
        getApiCount = count;
        RequestSpecification requestSpec = Utils.getDefaultSpec();
        // int i=0;
        for (int i = 1; i < count; i++) {

            BaseStepDef.response = RestAssured.given(requestSpec).header("Content-Type", "application/json").header("X-Registering-Institution-ID", sourceBBID)
                    .header("X-CallbackURL", identityMapperConfig.callbackURL + endpoint).queryParam("payeeIdentity", payeeIdentity)
                    .queryParam("paymentModality", "00").queryParam("requestId", generateUniqueNumber(10))
                    .queryParam("sourceBBID", sourceBBID).baseUri(identityMapperConfig.identityMapperContactPoint).expect()
                    .spec(new ResponseSpecBuilder().expectStatusCode(expectedStatus).build()).when()
                    .get(identityMapperConfig.accountLookupEndpoint).andReturn().asString();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }

    @Then("I should be able to verify that the {string} method to {string} endpoint received a request with required parameter in body")
    public void iShouldBeAbleToVerifyThatTheMethodToEndpointReceivedRequestWithASpecificBody(String httpmethod, String endpoint) {
        verify(putRequestedFor(urlEqualTo(endpoint)).withRequestBody(matchingJsonPath("$.registerRequestID", equalTo(requestId))));
        verify(putRequestedFor(urlEqualTo(endpoint)).withRequestBody(matchingJsonPath("$.numberFailedCases", equalTo("0"))));
    }

    @Then("I should be able to verify that the {string} method to {string} endpoint received a request with same payeeIdentity")
    public void iShouldBeAbleToVerifyThatTheMethodToEndpointReceivedARequestWithSamePayeeIdentity(String httpmethod, String endpoint) {
        verify(putRequestedFor(urlEqualTo(endpoint))
                .withRequestBody(matchingJsonPath("$.payeeIdentity", equalTo(payeeIdentity))));
    }

    public static String generateUniqueNumber(int length) {
        Random rand = new Random();
        long timestamp = System.currentTimeMillis();
        long randomLong = rand.nextLong(100000000);
        String uniqueNumber = timestamp + "" + randomLong;
        return uniqueNumber.substring(0, length);
    }

    @When("I call the register beneficiary API with expected status of {int}")
    public void iCallTheRegisterBeneficiaryAPIWithAMSISDNAndDFSPIDAs(int expectedStatus) {
        RequestSpecification requestSpec = Utils.getDefaultSpec();
        BaseStepDef.response = RestAssured.given(requestSpec).header("Content-Type", "application/json").header("X-Registering-Institution-ID", sourceBBID)
                .header("X-CallbackURL", identityMapperConfig.callbackURL + "/registerBeneficiary")
                .baseUri(identityMapperConfig.identityMapperContactPoint).body(registerBeneficiaryBody).expect()
                .spec(new ResponseSpecBuilder().expectStatusCode(expectedStatus).build()).when()
                .post(identityMapperConfig.registerBeneficiaryEndpoint).andReturn().asString();

        logger.info("Identity Mapper Response: {}", BaseStepDef.response);
    }

    @When("I create an IdentityMapperDTO for registering beneficiary with {string} as DFSPID")
    public void iCreateAnIdentityMapperDTOForRegisteringBeneficiaryWithAsDFSPID(String dfspId) {
        payeeDfspId = dfspId;

        List<BeneficiaryDTO> beneficiaryDTOList = new ArrayList<>();

        BeneficiaryDTO beneficiaryDTO = new BeneficiaryDTO(payeeIdentityAccountLookup, "01", "12345678", dfspId);
        beneficiaryDTOList.add(beneficiaryDTO);
        requestId = generateUniqueNumber(10);
        registerBeneficiaryBody = new AccountMapperRequestDTO(requestId, "SocialWelfare", beneficiaryDTOList);
    }

    @Then("I can call ops app transfer api with expected status of {int}")
    public void iCanCallOpsAppTransferApiWithExpectedStatusOf(int expectedStatus) {
        RequestSpecification requestSpec = Utils.getDefaultSpec(tenant);
        requestSpec.header("transactionId", transactionId);
        BaseStepDef.response = RestAssured.given(requestSpec).baseUri(channelConnectorConfig.channelConnectorContactPoint)
                .body(BaseStepDef.inboundTransferMockReq).expect().spec(new ResponseSpecBuilder().expectStatusCode(expectedStatus).build())
                .when().post(channelConnectorConfig.transferEndpoint).andReturn().asString();

        logger.info("Inbound transfer Response: {}", BaseStepDef.response);
    }

    @And("I can assert the payee DFSPID is same as used to register beneficiary id type from response")
    public void iCanAssertThePayeePartyIdTypeFromResponse() {
        String payeeDfsp = null;
        try {
            JSONArray jsonArray = new JSONArray(BaseStepDef.response);
            JSONObject transactionStatus = jsonArray.getJSONObject(0);
            payeeDfsp = transactionStatus.get("payeeDfspId").toString();

        } catch (Exception e) {
            logger.error("Error parsing the response", e);
        }
        assertThat(payeeDfspId).isEqualTo(payeeDfsp);
    }

    @When("I call the batch transactions endpoint with expected response status of {int}")
    public void callBatchTransactionsEndpoint(int expectedStatus) {
        RequestSpecification requestSpec = Utils.getDefaultSpec(BaseStepDef.tenant);
        requestSpec.header("filename", BaseStepDef.filename);
        requestSpec.header("X-CorrelationID", UUID.randomUUID().toString());
        requestSpec.queryParam("type", "CSV");
        BaseStepDef.response = RestAssured.given(requestSpec).baseUri(bulkProcessorConfig.bulkProcessorContactPoint)
                .contentType("multipart/form-data").multiPart("file", new File(Utils.getAbsoluteFilePathToResource(BaseStepDef.filename)))
                .expect().spec(new ResponseSpecBuilder().expectStatusCode(expectedStatus).build()).when()
                .post(bulkProcessorConfig.bulkTransactionEndpoint).andReturn().asString();

        logger.info("Batch Transactions API Response: " + BaseStepDef.response);
    }

    @Then("I should be able to parse batch id from response")
    public void iShouldBeAbleToParseBatchIdFromResponse() {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(BaseStepDef.response);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        String pollingPath = null;
        try {
            pollingPath = jsonObject.getString("PollingPath");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        // Extract the batch ID
        batchId = pollingPath.substring(pollingPath.lastIndexOf("/") + 1);
    }

    @When("I call the batch details API with expected response status of {int}")
    public void callBatchDetailsAPI(int expectedStatus) {
        RequestSpecification requestSpec = Utils.getDefaultSpec(BaseStepDef.tenant);
        if (authEnabled) {
            requestSpec.header("Authorization", "Bearer " + BaseStepDef.accessToken);
        }
        requestSpec.queryParam("batchId", batchId);

        BaseStepDef.response = RestAssured.given(requestSpec).baseUri(operationsAppConfig.operationAppContactPoint).expect()
                .spec(new ResponseSpecBuilder().expectStatusCode(expectedStatus).build()).when()
                .get(operationsAppConfig.batchDetailsEndpoint).andReturn().asString();

        logger.info("Batch Details Response: " + BaseStepDef.response);
    }

    @When("I create an IdentityMapperDTO for adding {int} beneficiary")
    public void iCreateAnIdentityMapperDTOForAddingBeneficiary(int count) {
        List<BeneficiaryDTO> beneficiaryDTOList = new ArrayList<>();
        for(int i=0;i<count;i++){
            BeneficiaryDTO beneficiaryDTO = new BeneficiaryDTO(generateUniqueNumber(18), null, null, null);
            beneficiaryList.add(beneficiaryDTO);
            beneficiaryDTOList.add(beneficiaryDTO);
        }
        requestId = generateUniqueNumber(14);
        registerBeneficiaryBody = new AccountMapperRequestDTO(requestId, sourceBBID, beneficiaryDTOList);
    }

    @When("I call the register beneficiary API with {string} as registering institution id expected status of {int} and stub {string}")
    public void iCallTheRegisterBeneficiaryAPIWithAsRegisteringInstitutionIdExpectedStatusOf(String registeringInstitutionId, int expectedStatus, String stub) {
        RequestSpecification requestSpec = Utils.getDefaultSpec();
        BaseStepDef.response = RestAssured.given(requestSpec).header("Content-Type", "application/json").header("X-Registering-Institution-ID", registeringInstitutionId)
                .header("X-CallbackURL", identityMapperConfig.callbackURL+ stub).baseUri(identityMapperConfig.identityMapperContactPoint)
                .body(registerBeneficiaryBody).expect().spec(new ResponseSpecBuilder().expectStatusCode(expectedStatus).build()).when()
                .post(identityMapperConfig.registerBeneficiaryEndpoint).andReturn().asString();

        logger.info("Identity Mapper Response: {}", BaseStepDef.response);
    }

    @Then("I call bulk account lookup API with these {int} beneficiaries and {string} as registering institution id and stub {string}")
    public void iCallBulkAccountLookupAPIWithTheseBeneficiariesAndAsRegisteringInstitutionId(int noOfBeneficiaries, String registeringInstitutionId, String stub) {
        RequestSpecification requestSpec = Utils.getDefaultSpec();
        BaseStepDef.response = RestAssured.given(requestSpec).header("Content-Type", "application/json").header("X-Registering-Institution-ID", registeringInstitutionId)
                .header("X-CallbackURL", identityMapperConfig.callbackURL + stub)
                .baseUri(identityMapperConfig.identityMapperContactPoint).body(batchAccountLookupBody).expect()
                .spec(new ResponseSpecBuilder().expectStatusCode(202).build()).when()
                .post(identityMapperConfig.batchAccountLookupEndpoint).andReturn().asString();

        logger.info("Identity Mapper Response: {}", BaseStepDef.response);
    }

    @And("I create request body for batch account lookup API")
    public void iCreateRequestBodyForBatchAccountLookupAPI() {
        batchAccountLookupBody = null;
        requestId = generateUniqueNumber(14);
        batchAccountLookupBody = new AccountMapperRequestDTO(requestId, "", beneficiaryList);
    }

    @And("I should be able to verify that the {string} method to {string} receive {int} request")
    public void iShouldBeAbleToVerifyThatTheMethodToReceiveRequest(String httpMethod, String stub, int noOfRequest) throws JsonProcessingException {
        List<ServeEvent> allServeEvents = getAllServeEvents();

        for(int i=0; i< allServeEvents.size();i++) {
            ServeEvent request = allServeEvents.get(i);

            if(!(request.getRequest().getBodyAsString()).isEmpty()) {
                try {
                    JsonNode rootNode = objectMapper.readTree(request.getRequest().getBodyAsString());
                    String requestID = rootNode.get("requestID").asText();

                    if (requestId.equals(requestID)) {
                        callbackBody = request.getRequest().getBodyAsString();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
        int count=0;
        try {
            JsonNode rootNode = objectMapper.readTree(callbackBody);

            JsonNode beneficiaryDTOList = rootNode.get("beneficiaryDTOList");
            if (beneficiaryDTOList.isArray()) {
                for (JsonNode beneficiary : beneficiaryDTOList) {
                    count++ ;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertThat(count).isEqualTo(noOfRequest);
        beneficiaryList = new ArrayList<>();
    }


    @When("I create an IdentityMapperDTO for {int} Register Beneficiary with payment modality as {string}")
    public void iCreateAnIdentityMapperDTOForRegisterBeneficiaryWithPaymentModalityAs(int noOfBeneficiary, String paymentModality) {
        List<BeneficiaryDTO> beneficiaryDTOList = new ArrayList<>();
        for(int i=0;i<noOfBeneficiary;i++) {
            BeneficiaryDTO beneficiaryDTO = new BeneficiaryDTO(generateUniqueNumber(16), getPaymentModality(paymentModality), "12345678", "ABCDEF");
            beneficiaryDTOList.add(beneficiaryDTO);
        }
        requestId = generateUniqueNumber(10);
        registerBeneficiaryBody = new AccountMapperRequestDTO(requestId, sourceBBID, beneficiaryDTOList);
    }

    public String getPaymentModality(String paymentModality){
        return paymentModalityList.get(paymentModality);
    }

    @When("I call the update beneficiary API with expected status of {int} and stub {string}")
    public void iCallTheUpdateBeneficiaryAPIWithExpectedStatusOfAndStub(int expectedStatus, String stub) {
        RequestSpecification requestSpec = Utils.getDefaultSpec();
        BaseStepDef.response = RestAssured.given(requestSpec).header("Content-Type", "application/json").header("X-Registering-Institution-ID", sourceBBID)
                .header("X-CallbackURL", identityMapperConfig.callbackURL + stub).baseUri(identityMapperConfig.identityMapperContactPoint)
                .body(registerBeneficiaryBody).expect().spec(new ResponseSpecBuilder().expectStatusCode(expectedStatus).build()).when()
                .put(identityMapperConfig.registerBeneficiaryEndpoint).andReturn().asString();

        logger.info("Identity Mapper Response: {}", BaseStepDef.response);

    }

    @When("I create an IdentityMapperDTO for {int} update Beneficiary with payment modality as {string}")
    public void iCreateAnIdentityMapperDTOForUpdateBeneficiaryWithPaymentModalityAs(int noOfBeneficiary, String paymentModality) {
        List<BeneficiaryDTO> beneficiaryDTOList = new ArrayList<>();
        for(int i=0;i<noOfBeneficiary;i++) {
            BeneficiaryDTO beneficiaryDTO =  registerBeneficiaryBody.getBeneficiaries().get(i);
            beneficiaryDTO.setPaymentModality(getPaymentModality(paymentModality));
           // BeneficiaryDTO beneficiaryDTO = new BeneficiaryDTO("94049169714828912115", getPaymentModality(paymentModality), "12345678", "ABCDEF");
            beneficiaryDTOList.add(beneficiaryDTO);
        }
        requestId = generateUniqueNumber(10);
        registerBeneficiaryBody = new AccountMapperRequestDTO(requestId, sourceBBID, beneficiaryDTOList);
    }

    @When("I create an IdentityMapperDTO for {int} update Beneficiary with banking institution code as {string}")
    public void iCreateAnIdentityMapperDTOForUpdateBeneficiaryWithBankingInstitutionCodeAs(int noOfBeneficiary, String bankingInstitutionCode) {
        List<BeneficiaryDTO> beneficiaryDTOList = new ArrayList<>();
        for(int i=0;i<noOfBeneficiary;i++) {
            BeneficiaryDTO beneficiaryDTO =  registerBeneficiaryBody.getBeneficiaries().get(i);
            beneficiaryDTO.setBankingInstitutionCode(bankingInstitutionCode);
            //BeneficiaryDTO beneficiaryDTO = new BeneficiaryDTO("94049169714828912115","00", "12345678", bankingInstitutionCode);
            beneficiaryDTOList.add(beneficiaryDTO);
        }
        requestId = generateUniqueNumber(10);
        registerBeneficiaryBody = new AccountMapperRequestDTO(requestId, sourceBBID, beneficiaryDTOList);
    }

    @When("I create an IdentityMapperDTO for {int} update Beneficiary with financial address code as {string}")
    public void iCreateAnIdentityMapperDTOForUpdateBeneficiaryWithFinancialAddressCodeAs(int noOfBeneficiary, String financialAddress) {
        List<BeneficiaryDTO> beneficiaryDTOList = new ArrayList<>();
        for(int i=0;i<noOfBeneficiary;i++) {
            BeneficiaryDTO beneficiaryDTO =  registerBeneficiaryBody.getBeneficiaries().get(i);
            beneficiaryDTO.setFinancialAddress(financialAddress);
            //BeneficiaryDTO beneficiaryDTO = new BeneficiaryDTO("94049169714828912115","00", "12345678", bankingInstitutionCode);
            beneficiaryDTOList.add(beneficiaryDTO);
        }
        requestId = generateUniqueNumber(10);
        registerBeneficiaryBody = new AccountMapperRequestDTO(requestId, sourceBBID, beneficiaryDTOList);
    }
}
