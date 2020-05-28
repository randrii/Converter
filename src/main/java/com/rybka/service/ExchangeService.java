package com.rybka.service;

import com.rybka.exception.CurrencyAPICallException;
import com.rybka.exception.ZeroCurrencyRateException;
import com.rybka.model.ConvertedCurrency;
import com.rybka.model.Currency;
import com.rybka.model.ExchangeRate;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class ExchangeService {

    private ApiConnectorService connectorService = new ApiConnectorService();

    public ExchangeRate loadCurrencyOf(String userBaseCurrency, String userTargetCurrency) throws CurrencyAPICallException, ZeroCurrencyRateException {
        var currencyRateMap = connectorService.retrieveRates(userBaseCurrency);
        var currencyRateList = createCurrencyObjectList(currencyRateMap);
        var currencyRate = retrieveJsonData(currencyRateList, userTargetCurrency);
        if (currencyRate == 0.0) {
            throw new ZeroCurrencyRateException("Invalid currency");
        }
        return new ExchangeRate(userBaseCurrency, userTargetCurrency, currencyRate);
    }

    public ConvertedCurrency calculateTotal(ExchangeRate exchangeRate, Double amount) {
        ConvertedCurrency currency = new ConvertedCurrency();
        currency.setBase(exchangeRate.getBase());
        currency.setTarget(exchangeRate.getTarget());
        currency.setRate(exchangeRate.getRate());
        currency.setAmount(amount);
        currency.setTotal(currency.getAmount() * currency.getRate());
        return currency;
    }

    public Double retrieveJsonData(List<Currency> currencies, String userTargetCurrency) {
        var currencyRate = 0.0;
        currencyRate = currencies.stream()
                .filter(element -> userTargetCurrency.equals(element.getName()))
                .findFirst()
                .get()
                .getValue();

        return currencyRate;
    }

    public List<Currency> createCurrencyObjectList(Map<String, Double> map) {
        return map.entrySet()
                .stream()
                .map(item -> new Currency(item.getKey(), item.getValue()))
                .collect(Collectors.toList());
    }
}