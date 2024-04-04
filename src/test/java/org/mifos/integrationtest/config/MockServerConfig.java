package org.mifos.integrationtest.config;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.extern.slf4j.Slf4j;
import org.mifos.integrationtest.cucumber.stepdef.ScenarioScopeState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class MockServerConfig implements MockServer {

    @Autowired
    ScenarioScopeState scenarioScopeState;

    private static WireMockServer singleInstance = null;

    public WireMockServer getMockServer() {
        if (MockServerConfig.singleInstance == null) {
            MockServerConfig.singleInstance = new WireMockServer(wireMockConfig().port(scenarioScopeState.mockServerPort));
            // log.debug("PORT {}", port);
        }

        return MockServerConfig.singleInstance;
    }

    @Override
    public String getBaseUri() {
        return "http://localhost:" + "4040";
    }

}
