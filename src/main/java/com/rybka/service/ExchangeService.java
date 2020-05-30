package com.rybka.service;

import com.rybka.exception.ZeroCurrencyRateException;
import com.rybka.model.Currency;
import com.rybka.model.ExchangeResponse;
import lombok.Data;

import java.util.Map;

@Data
public class ExchangeService {

    private ApiConnectorService connectorService = new ApiConnectorService();

    public ExchangeResponse loadCurrencyOf(String userBaseCurrency) {
        return connectorService.retrieveRates(userBaseCurrency);
    }

    public Currency calculateTotal(ExchangeResponse exchangeResponse, String userTargetCurrency, Double amount) {
        var targetCurrency = retrieveTargetCurrency(exchangeResponse, userTargetCurrency);
        Currency currency = new Currency();

        currency.setBase(exchangeResponse.getBase());
        currency.setTarget(targetCurrency.getKey());
        currency.setAmount(amount);
        currency.setRate(targetCurrency.getValue());
        currency.setTotal(currency.getAmount() * currency.getRate());
        return currency;
    }

    public Map.Entry<String, Double> retrieveTargetCurrency(ExchangeResponse exchangeResponse, String userTargetCurrency) {

        return exchangeResponse.getRates().entrySet().stream()
                .filter(item -> userTargetCurrency.equals(item.getKey()))
                .findFirst()
                .orElseThrow(ZeroCurrencyRateException::new);
    }
}