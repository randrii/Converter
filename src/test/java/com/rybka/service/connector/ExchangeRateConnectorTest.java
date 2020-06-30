package com.rybka.service.connector;

import com.rybka.constant.ParameterValue;
import com.rybka.model.ExchangeResponse;
import coresearch.cvurl.io.request.CVurl;
import coresearch.cvurl.io.request.RequestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class ExchangeRateConnectorTest {

    @Mock
    private CVurl cVurl;
    @Mock
    private RequestBuilder requestBuilder;

    private ExchangeRateConnector connector;

    private ExchangeResponse response;

    @BeforeEach
    public void setUp() {
        connector = new ExchangeRateConnector(cVurl);
        response = new ExchangeResponse();
        response.setBase(ParameterValue.USD);
        response.setRates(Map.of(ParameterValue.EUR, ParameterValue.EUR_RATE));
    }

    @Test
    public void testConnector() {
        when(cVurl.get(Mockito.anyString())).thenReturn(requestBuilder);
        when(requestBuilder.asObject(ExchangeResponse.class)).thenReturn(response);
        assertEquals(ParameterValue.EUR_RATE, connector.retrieveRates(ParameterValue.EUR).getRates().get(ParameterValue.EUR));
    }
}