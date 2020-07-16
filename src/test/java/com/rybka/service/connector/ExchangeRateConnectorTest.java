package com.rybka.service.connector;

import com.rybka.exception.CurrencyAPICallException;
import com.rybka.model.dto.ExchangeResponse;
import com.rybka.properties.ExchangeConnectorProperty;
import coresearch.cvurl.io.request.CVurl;
import coresearch.cvurl.io.request.RequestBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class ExchangeRateConnectorTest {

    @Mock
    private CVurl cVurl;
    @Mock
    private ExchangeConnectorProperty connectorProperty;
    @Mock
    private RequestBuilder requestBuilder;
    @InjectMocks
    private ExchangeRateConnector connector;

    @Test
    public void testOnProperWork() {

        // given
        var currencyBase = "USD";
        var currencyTarget = "EUR";
        var currencyRate = 0.89;
        var response = new ExchangeResponse();
        var testUrl = "http://example.com";

        response.setBase(currencyBase);
        response.setRates(Map.of(currencyTarget, currencyRate));

        when(connectorProperty.getUrl()).thenReturn(testUrl);
        when(cVurl.get(String.format(connectorProperty.getUrl(), currencyBase))).thenReturn(requestBuilder);
        when(requestBuilder.asObject(ExchangeResponse.class)).thenReturn(response);

        // when
        var actualResult = connector.retrieveRates(currencyBase);

        // then
        assertEquals(currencyRate, actualResult.getRates().get(currencyTarget));
    }

    @Test
    public void testOnIncorrectCurrency() {

        // given
        var incorrectCurrencyBase = "USDD";
        var currencyTarget = "EUR";
        var response = new ExchangeResponse();
        var testUrl = "http://example.com";

        response.setBase(incorrectCurrencyBase);
        response.setRates(Map.of());

        when(connectorProperty.getUrl()).thenReturn(testUrl);
        when(cVurl.get(Mockito.anyString())).thenReturn(requestBuilder);
        when(requestBuilder.asObject(ExchangeResponse.class)).thenReturn(response);

        // when
        var result = connector.retrieveRates(currencyTarget);

        // then
        assertNull(result.getRates().get(currencyTarget));
    }

    @Test
    public void testOnCurrencyAPICallException() {

        // given
        var currencyTarget = "EUR";
        var testUrl = "http://example.com";

        when(connectorProperty.getUrl()).thenReturn(testUrl);
        when(cVurl.get(Mockito.anyString())).thenReturn(null);

        // then
        assertThrows(CurrencyAPICallException.class, () -> connector.retrieveRates(currencyTarget));
    }
}