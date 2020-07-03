package com.rybka.service.exchange;

import com.rybka.configuration.ExchangeProperty;
import com.rybka.exception.MissedBaseCurrencyException;
import com.rybka.model.ExchangeResponse;
import com.rybka.model.TopCurrencyData;
import com.rybka.service.connector.ExchangeRateConnector;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class ExchangeServiceTest {

    @Mock
    private ExchangeRateConnector exchangeRateConnector;
    @Mock
    private ExchangeProperty exchangeProperty;
    @InjectMocks
    private ExchangeService exchangeService;

    @Test
    public void testOnProperWork() {

        // given
        var currencyBase = "USD";
        var currencyTarget = "EUR";
        var currencyRate = 0.89;
        var response = constructResponse(currencyBase, Map.of(currencyTarget, currencyRate));

        when(exchangeRateConnector.retrieveRates(currencyBase)).thenReturn(response);

        // when
        var actualResult = exchangeService.loadCurrencyOf(currencyBase, currencyTarget);

        // then
        assertEquals(currencyRate, actualResult.getRate());
    }

    @Test
    public void testOnIncorrectCurrency() {

        // given
        var currencyTarget = "EUR";
        var incorrectCurrencyRate = 0.0;
        var incorrectCurrencyTarget = "UAH";
        var response = constructResponse(incorrectCurrencyTarget, Map.of(currencyTarget, incorrectCurrencyRate));

        when(exchangeRateConnector.retrieveRates(incorrectCurrencyTarget)).thenReturn(response);

        // when
        var actualResult = exchangeService.loadCurrencyOf(incorrectCurrencyTarget, currencyTarget);

        // then
        assertEquals(incorrectCurrencyRate, actualResult.getRate());
    }

    @Test
    public void testOnCalculation() {

        // given
        var currencyRate = 0.89;
        var currencyAmount = 4d;

        // when
        var expectedResult = currencyRate * currencyAmount;
        var actualResult = exchangeService.calculateTotal(currencyRate, currencyAmount);

        // then
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testOnRetrievingTopCurrency() {

        // given
        var currencyAmount = 4d;
        var currencyBase = "USD";
        var exchangeType = "SELL";
        var currencyTarget = "EUR";
        var currencyRate = 0.89;
        var topCurrencies = "UAH,USD,EUR,PLN";
        var response = constructResponse(currencyBase, Map.of(currencyTarget, currencyRate));
        Map<String, BiFunction<String, Double, List<TopCurrencyData>>> exchangeTypeMap = new HashMap<>();

        exchangeService.setExchangeTypeMap(exchangeTypeMap);
        exchangeService.initExchangeTypeMap();

        when(exchangeProperty.getCurrency()).thenReturn(topCurrencies);
        when(exchangeRateConnector.retrieveRates(currencyBase)).thenReturn(response);

        // when
        var actualResult = exchangeService.retrieveTopCurrency(currencyBase, exchangeType, currencyAmount);

        // then
        assertEquals(currencyAmount, actualResult.getTopCurrencyDataList().size());
    }

    @Test
    public void testOnBuyingTopCurrency() {

        // given
        var currencyAmount = 4d;
        var currencyBase = "USD";
        var exchangeType = "BUY";
        var currencyTarget = "EUR";
        var currencyRate = 0.89;
        var topCurrencies = "UAH,USD,EUR,PLN";
        var response = constructResponse(currencyBase, Map.of(currencyTarget, currencyRate));
        Map<String, BiFunction<String, Double, List<TopCurrencyData>>> exchangeTypeMap = new HashMap<>();

        exchangeService.setExchangeTypeMap(exchangeTypeMap);
        exchangeService.initExchangeTypeMap();

        when(exchangeProperty.getCurrency()).thenReturn(topCurrencies);
        when(exchangeRateConnector.retrieveRates(Mockito.anyString())).thenReturn(response);

        // when
        var actualResult = exchangeService.retrieveTopCurrency(currencyBase, exchangeType, currencyAmount);

        // then
        assertEquals(currencyAmount, actualResult.getTopCurrencyDataList().size());
    }

    @Test
    public void testOnMissedBaseCurrency() {

        // given
        var missedCurrencyBase = "";
        var currencyTarget = "EUR";
        var response = constructResponse(missedCurrencyBase, Map.of());

        when(exchangeRateConnector.retrieveRates(missedCurrencyBase)).thenReturn(response);

        // then
        assertThrows(MissedBaseCurrencyException.class, () -> exchangeService.loadCurrencyOf(missedCurrencyBase, currencyTarget));
    }

    private ExchangeResponse constructResponse(String base, Map<String, Double> rates) {
        var response = new ExchangeResponse();
        response.setBase(base);
        response.setRates(rates);
        return response;
    }
}