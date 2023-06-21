package org.mifos.integrationtest.config;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MockServerConfig implements MockServer {

    private static WireMockServer singleInstance = null;

    @Value("${mock-server.port}")
    private int port;

    public WireMockServer getMockServer() {
        if (MockServerConfig.singleInstance == null) {
            MockServerConfig.singleInstance = new WireMockServer(wireMockConfig().port(this.port));
            System.out.println("PORT: " + port);
        }

        return MockServerConfig.singleInstance;
    }

    @Override
    public String getBaseUri() {
        return "http://localhost:" + getMockServer().port();
    }

}
