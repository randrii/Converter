package com.rybka.service.exchange;

import com.rybka.exception.MissedBaseCurrencyException;
import com.rybka.model.CurrencyData;
import com.rybka.model.ExchangeResponse;
import com.rybka.service.connector.BaseCurrencyExchangeConnector;
import lombok.Data;

import java.util.Map;

@Data
public class ExchangeService {

    private BaseCurrencyExchangeConnector connectorService;

    public ExchangeService(BaseCurrencyExchangeConnector connectorService) {
        this.connectorService = connectorService;
    }

    public CurrencyData loadCurrencyOf(String userBaseCurrency, String userTargetCurrency) {
        var exchangeResponse = connectorService.retrieveRates(userBaseCurrency);
        var targetCurrency = retrieveTargetCurrency(exchangeResponse, userTargetCurrency);
        return new CurrencyData(userBaseCurrency, targetCurrency.getKey(), targetCurrency.getValue());
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