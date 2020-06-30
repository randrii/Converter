package com.rybka.service.exchange;

import com.rybka.configuration.ExchangeProperty;
import com.rybka.constant.ParameterValue;
import com.rybka.model.ExchangeResponse;
import com.rybka.model.TopCurrencyData;
import com.rybka.service.connector.ExchangeRateConnector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class ExchangeServiceTest {

    @Mock
    private ExchangeRateConnector exchangeRateConnector;
    @Mock
    private ExchangeProperty exchangeProperty;
    @Mock
    private Map<String, BiFunction<String, Double, List<TopCurrencyData>>> exchangeTypeMap;

    private ExchangeService exchangeService;

    private ExchangeResponse response;

    @BeforeEach
    public void setUp() {
        exchangeProperty = new ExchangeProperty();
        exchangeProperty.setSource(ParameterValue.USD);
        exchangeProperty.setCurrency(ParameterValue.EXCHANGE_CURRENCY);
        exchangeTypeMap = new HashMap<>();
        response = constructResponse(ParameterValue.USD, Map.of(ParameterValue.EUR, ParameterValue.EUR_RATE));
        exchangeService = new ExchangeService(exchangeRateConnector, exchangeProperty, exchangeTypeMap);
    }

    @Test
    public void testOnProperWork() {
        when(exchangeRateConnector.retrieveRates(ParameterValue.USD)).thenReturn(response);
        assertEquals(ParameterValue.EUR_RATE, exchangeService.loadCurrencyOf(ParameterValue.USD, ParameterValue.EUR).getRate());
    }

    @Test
    public void testOnIncorrectCurrency() {
        response = constructResponse(ParameterValue.UAH, Map.of(ParameterValue.EUR, ParameterValue.INCORRECT_RATE));

        when(exchangeRateConnector.retrieveRates(ParameterValue.UAH)).thenReturn(response);
        assertEquals(ParameterValue.INCORRECT_RATE, exchangeService.loadCurrencyOf(ParameterValue.UAH, ParameterValue.EUR).getRate());
    }

    @Test
    public void testOnCalculation() {
        assertEquals(ParameterValue.EUR_RATE * ParameterValue.CURRENCY_AMOUNT,
                exchangeService.calculateTotal(ParameterValue.EUR_RATE, ParameterValue.CURRENCY_AMOUNT));
    }

    @Test
    public void testOnRetrievingTopCurrency() {
        when(exchangeRateConnector.retrieveRates(ParameterValue.USD)).thenReturn(response);
        assertEquals(4, exchangeService.retrieveTopCurrency(ParameterValue.USD, ParameterValue.EXCHANGE_TYPE, ParameterValue.CURRENCY_AMOUNT).getTopCurrencyDataList().size());
    }

    private ExchangeResponse constructResponse(String base, Map<String, Double> rates) {
        var response = new ExchangeResponse();
        response.setBase(base);
        response.setRates(rates);
        return response;
    }
}