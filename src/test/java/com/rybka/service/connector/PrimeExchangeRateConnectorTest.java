package com.rybka.service.connector;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.rybka.configuration.PrimeExchangeConnectorProperties;
import com.rybka.configuration.WireMockInitializer;
import com.rybka.exception.CurrencyAPICallException;
import com.rybka.model.ExchangeResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;

import java.net.http.HttpClient;
import java.nio.file.Files;

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
    private PrimeExchangeConnectorProperties primeExchangeConnectorProperties;
    private PrimeExchangeRateConnector connector;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @BeforeEach
    public void setUp() {
        connector = new PrimeExchangeRateConnector(httpClient, objectMapper, primeExchangeConnectorProperties);

        when(primeExchangeConnectorProperties.getHost()).thenReturn(wireMockServer.baseUrl());
        when(primeExchangeConnectorProperties.getEndpoint()).thenReturn("/v5/%s/latest/%s");
        when(primeExchangeConnectorProperties.getSchema()).thenReturn("");
        when(primeExchangeConnectorProperties.getToken()).thenReturn("4e0bbdc44fcdf05612fa0882");
    }

    @SneakyThrows
    @Test
    public void testOnProperWork() {

        // given
        var currencyBase = "USD";
        var primeUrl = String.format("/v5/%s/latest/%s", "4e0bbdc44fcdf05612fa0882", currencyBase);
        var testJsonFile = "primeExchangeResponseTest.json";
        var primeExchangeApiResponseJson = retrieveResource(testJsonFile);
        var actualResult = objectMapper.readValue(primeExchangeApiResponseJson, ExchangeResponse.class);

        wireMockServer.stubFor(get(urlEqualTo(primeUrl))
                .willReturn(aResponse().withBody(primeExchangeApiResponseJson)));

        // when
        var result = connector.retrieveRates(currencyBase);

        // then
        assertEquals(result, actualResult);
    }

    @Test
    public void testOnException() {

        // given
        var currencyBase = "USD";
        var primeUrl = String.format("/v5/%s/latest/%s", "4e0bbdc44fcdf05612fa0882", currencyBase);
        var brokenPrimeExchangeApiResponseJson = "";

        wireMockServer.stubFor(get(urlEqualTo(primeUrl)).willReturn(aResponse().withBody(brokenPrimeExchangeApiResponseJson)));

        // then
        assertThrows(CurrencyAPICallException.class, () -> connector.retrieveRates(currencyBase));
    }

    @SneakyThrows
    private String retrieveResource(String fileName) {
        var testFile = new ClassPathResource(fileName);

        return Files.readString(testFile.getFile().toPath());
    }
}