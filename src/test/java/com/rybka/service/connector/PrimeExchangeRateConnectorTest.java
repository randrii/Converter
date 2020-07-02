package com.rybka.service.connector;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.rybka.configuration.ApplicationProperties;
import com.rybka.configuration.WireMockInitializer;
import com.rybka.exception.CurrencyAPICallException;
import com.rybka.model.ExchangeResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.net.http.HttpClient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration(initializers = WireMockInitializer.class)
public class PrimeExchangeRateConnectorTest {
    @Autowired
    private WireMockServer wireMockServer;
    @Mock
    private ApplicationProperties applicationProperties;
    private PrimeExchangeRateConnector connector;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @BeforeEach
    public void setUp() {
        connector = new PrimeExchangeRateConnector(httpClient, objectMapper, applicationProperties);

        when(applicationProperties.getHost()).thenReturn(wireMockServer.baseUrl());
        when(applicationProperties.getEndpoint()).thenReturn("/v5/%s/latest/%s");
        when(applicationProperties.getSchema()).thenReturn("");
        when(applicationProperties.getToken()).thenReturn("4e0bbdc44fcdf05612fa0882");
    }

    @SneakyThrows
    @Test
    public void testOnProperWork() {

        // given
        var currencyBase = "USD";
        var currencyTarget = "EUR";
        var currencyRate = 0.893333;
        var primeUrl = String.format("/v5/%s/latest/%s", "4e0bbdc44fcdf05612fa0882", currencyBase);
        var json = "{" +
                "  \"rates\": {" +
                "    \"" + currencyTarget + "\": " + currencyRate + "  }" +
                "}";
        var actualResult = objectMapper.readValue(json, ExchangeResponse.class);

        configureFor("localhost", 8080);
        wireMockServer.stubFor(get(urlEqualTo(primeUrl))
                .willReturn(aResponse().withBody(json)));

        // when
        var result = connector.retrieveRates(currencyBase);

        // then
        assertEquals(result, actualResult);
    }

    @SneakyThrows
    @Test
    public void testOnException() {

        // given
        var currencyBase = "USD";
        var primeUrl = String.format("/v5/%s/latest/%s", "4e0bbdc44fcdf05612fa0882", currencyBase);

        configureFor("localhost", 8080);
        wireMockServer.stubFor(get(urlEqualTo(primeUrl)).willReturn(aResponse().withBody("")));

        // then
        assertThrows(CurrencyAPICallException.class, () -> connector.retrieveRates(currencyBase));
    }
}