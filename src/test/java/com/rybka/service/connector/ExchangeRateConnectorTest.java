package com.rybka.service.connector;

import com.rybka.constant.CurrencyAPIConstants;
import com.rybka.exception.CurrencyAPICallException;
import com.rybka.model.ExchangeResponse;
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

        response.setBase(currencyBase);
        response.setRates(Map.of(currencyTarget, currencyRate));

        when(cVurl.get(String.format(CurrencyAPIConstants.EXCHANGE_RATE_API_URL, currencyBase))).thenReturn(requestBuilder);
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

        response.setBase(incorrectCurrencyBase);
        response.setRates(Map.of());

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

        when(cVurl.get(Mockito.anyString())).thenReturn(null);

        // then
        assertThrows(CurrencyAPICallException.class, () -> connector.retrieveRates(currencyTarget));
    }
}