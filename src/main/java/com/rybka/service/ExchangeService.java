package com.rybka.service;

import com.rybka.exception.MissedBaseCurrencyException;
import com.rybka.model.CurrencyResponse;
import com.rybka.model.ExchangeResponse;
import lombok.Data;

import java.util.Map;

@Data
public class ExchangeService {

    private ApiConnectorService connectorService = new ApiConnectorService();

    public CurrencyResponse loadCurrencyOf(String userBaseCurrency, String userTargetCurrency) {
        var exchangeResponse = connectorService.retrieveRates(userBaseCurrency);
        var targetCurrency = retrieveTargetCurrency(exchangeResponse, userTargetCurrency);
        return new CurrencyResponse(userBaseCurrency, targetCurrency.getKey(), targetCurrency.getValue());
    }

    public Double calculateTotal(Double rate, Double amount) {
        return rate * amount;
    }

    private Map.Entry<String, Double> retrieveTargetCurrency(ExchangeResponse exchangeResponse, String userTargetCurrency) {

        return exchangeResponse.getRates().entrySet().stream()
                .filter(item -> userTargetCurrency.equals(item.getKey()))
                .findFirst()
                .orElseThrow(() -> new MissedBaseCurrencyException("Target currency not found!"));
    }
}