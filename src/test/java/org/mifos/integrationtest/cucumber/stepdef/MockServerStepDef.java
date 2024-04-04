package org.mifos.integrationtest.cucumber.stepdef;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.deleteRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.status;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.google.common.truth.Truth.assertThat;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.mifos.integrationtest.common.Utils.getDefaultSpec;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSender;
import java.util.concurrent.atomic.AtomicInteger;
import org.mifos.integrationtest.common.HttpMethod;

public class MockServerStepDef extends BaseStepDef {

    private static Boolean wiremockStarted = false;
    private static final AtomicInteger portCounter = new AtomicInteger(53013);
    private static WireMockServer wireMockServer;

    @Given("I can inject MockServer")
    public void checkIfMockServerIsInjected() {
        assertThat(mockServer).isNotNull();
    }

    @Then("I should be able to get instance of mock server")
    public void getInstanceOfMockServer() throws InterruptedException {
        assertThat(mockServer.getMockServer()).isNotNull();
        assertThat(mockServer.getMockServer().port()).isEqualTo(scenarioScopeState.mockServerPort);
    }

    @ParameterType(name = "httpMethod", value = ".*")
    public HttpMethod httpMethod(String httpMethod) {
        httpMethod = httpMethod.replace("\"", "");
        logger.debug("HTTP METHOD: $$$$$$: {}", httpMethod);
        return HttpMethod.valueOf(httpMethod);
    }

    @And("I can register the stub with {string} endpoint for {httpMethod} request with status of {int}")
    public void startStub(String endpoint, HttpMethod httpMethod, int status) {
        switch (httpMethod) {
            case GET -> {
                // wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo(endpoint)).willReturn(WireMock.aResponse().withStatus(200)));
                mockServer.getMockServer().stubFor(get(urlPathMatching(endpoint)).willReturn(status(status)));

            }
            case POST -> {
                // wireMockServer.stubFor(WireMock.post(WireMock.urlEqualTo(endpoint)).willReturn(WireMock.aResponse().withStatus(200)));
                mockServer.getMockServer().stubFor(post(urlPathMatching(endpoint)).willReturn(status(status)));
                // configureFor("localhost",4040);
            }
            case PUT -> {
                // wireMockServer.stubFor(WireMock.put(WireMock.urlEqualTo(endpoint)).willReturn(WireMock.aResponse().withStatus(200)));
                mockServer.getMockServer().stubFor(put(urlPathMatching(endpoint)).willReturn(status(status)));
            }
            case DELETE -> {
                wireMockServer.stubFor(WireMock.delete(WireMock.urlEqualTo(endpoint)).willReturn(WireMock.aResponse().withStatus(200)));
                // mockServer.getMockServer().stubFor(delete(urlPathMatching(endpoint)).willReturn(status(status)));
            }
        }
    }

    @When("I make the {httpMethod} request to {string} endpoint with expected status of {int}")
    public void hitStubEndpoint(HttpMethod httpMethod, String endpoint, int expectedStatus) {
        RequestSender requestSender = RestAssured.given(getDefaultSpec()).baseUri(mockServer.getBaseUri()).expect()
                .spec(new ResponseSpecBuilder().expectStatusCode(expectedStatus).build()).when();

        switch (httpMethod) {
            case GET -> {
                requestSender.get(endpoint);
            }
            case POST -> {
                requestSender.post(endpoint).andReturn().asString();
            }
            case PUT -> {
                requestSender.put(endpoint).andReturn().asString();
            }
            case DELETE -> {
                requestSender.delete(endpoint);
            }
        }
    }

    @Then("I should be able to verify that the {httpMethod} method to {string} endpoint received {int} request")
    public void verifyStub(HttpMethod httpMethod, String endpoint, int numberOfRequest) {
        await().atMost(awaitMost, SECONDS).pollInterval(pollInterval, SECONDS).untilAsserted(() -> {
            switch (httpMethod) {
                case GET -> {
                    mockServer.getMockServer().verify(numberOfRequest, getRequestedFor(urlEqualTo(endpoint)));
                }
                case POST -> {
                    mockServer.getMockServer().verify(numberOfRequest, postRequestedFor(urlEqualTo(endpoint)));
                }
                case PUT -> {
                    mockServer.getMockServer().verify(numberOfRequest, putRequestedFor(urlEqualTo(endpoint)));
                }
                case DELETE -> {
                    mockServer.getMockServer().verify(numberOfRequest, deleteRequestedFor(urlEqualTo(endpoint)));
                }
            }
        });
    }

    @And("I can start mock server")
    public void startMockServer() {
        mockServer.getMockServer().start();
        logger.info("PORT {}", scenarioScopeState.mockServerPort);
        configureFor(scenarioScopeState.mockServerPort);
    }

    @And("I can stop mock server")
    public void stopMockServer() {
        mockServer.getMockServer().stop();
    }

    @Given("I will start the mock server")
    public void iWillStartTheMockServer() {
        if (!wiremockStarted) {
            checkIfMockServerIsInjected();
            startMockServer();
        }
    }

    @And("I will register the stub with {string} endpoint for {httpMethod} request with status of {int}")
    public void iWillRegisterTheStubWithEndpointForRequestWithStatusOf(String endpoint, HttpMethod httpMethod, int status) {
        if (!wiremockStarted) {
            startStub(endpoint, httpMethod, status);

        }
    }

    @Then("I will update the  mock server and register stub as done")
    public void iWillUpdateTheMockServerAndRegisterStubAsDone() {
        wiremockStarted = true;
    }

    @Given("I will assign a port to mock server")
    public void iWillAssignAPortToMockServer() {
        int port = portCounter.getAndIncrement();
        scenarioScopeState.mockServerPort = port;
    }

}
