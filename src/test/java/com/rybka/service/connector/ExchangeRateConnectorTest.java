package com.rybka.service.connector;

import com.rybka.configuration.ExchangeProperty;
import com.rybka.model.ExchangeResponse;
import com.rybka.service.exchange.ExchangeService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
class ExchangeRateConnectorTest {

    @Mock
    ExchangeRateConnector exchangeRateConnector;
    @Mock
    static ExchangeProperty exchangeProperty;
    @InjectMocks
    ExchangeService exchangeService;

    static ExchangeResponse response;

    @BeforeAll
    static void setUp() {
        exchangeProperty = new ExchangeProperty();
        exchangeProperty.setSource("exchange");

        response = new ExchangeResponse();
        response.setBase("USD");
        response.setRates(Map.of("EUR", 0.89));
    }

    @Test
    public void testOnProperWork() {
        when(exchangeRateConnector.retrieveRates("USD")).thenReturn(response);
        assertEquals(0.89, exchangeService.loadCurrencyOf("USD", "EUR").getRate());
    }

    @Test
    public void testOnIncorrectCurrency() {
        response.setBase("UAH");
        response.setRates(Map.of("EUR", 0.0));

        when(exchangeRateConnector.retrieveRates("UAH")).thenReturn(response);
        assertEquals(0.0, exchangeService.loadCurrencyOf("UAH", "EUR").getRate());
    }

    @Test
    public void testOnException() {
        assertThrows(NullPointerException.class, () -> exchangeService.loadCurrencyOf("error", "EUR"));
    }
}