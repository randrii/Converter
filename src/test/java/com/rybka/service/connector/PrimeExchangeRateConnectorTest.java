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
class PrimeExchangeRateConnectorTest {

    @Mock
    private PrimeExchangeRateConnector primeExchangeRateConnector;

    @Mock
    private static ExchangeProperty exchangeProperty;

    @InjectMocks
    private ExchangeService exchangeService;

    private static ExchangeResponse response;

    @BeforeAll
    static void setUp() {
        exchangeProperty = new ExchangeProperty();
        exchangeProperty.setSource("prime_exchange");

        response = new ExchangeResponse();
        response.setBase("USD");
        response.setRates(Map.of("EUR", 0.89, "UAH", 26.68));
    }

    @Test
    public void testOnProperWork() {
        when(primeExchangeRateConnector.retrieveRates("USD")).thenReturn(response);
        assertEquals(0.89, exchangeService.loadCurrencyOf("USD", "EUR").getRate());
        assertEquals(26.68, exchangeService.loadCurrencyOf("USD", "UAH").getRate());
    }

    @Test
    public void testOnIncorrectCurrency() {
        response.setBase("UAH");
        response.setRates(Map.of("EUR", 0.033));

        when(primeExchangeRateConnector.retrieveRates("UAH")).thenReturn(response);
        assertEquals(0.033, exchangeService.loadCurrencyOf("UAH", "EUR").getRate());
    }

    @Test
    public void testOnException() {
        assertThrows(NullPointerException.class, () -> exchangeService.loadCurrencyOf("error", "EUR"));
    }
}