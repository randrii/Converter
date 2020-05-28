package com.rybka.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rybka.exception.NullResponseException;
import com.rybka.exception.ZeroCurrencyRateException;
import com.rybka.model.ConvertedCurrency;
import com.rybka.model.ExchangeRate;
import lombok.Data;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@Data
public class ExchangeService {


    public ExchangeRate loadCurrencyOf(String userBaseCurrency, String userTargetCurrency) throws NullResponseException, ZeroCurrencyRateException {
        var response = new ApiConnectorService().retrieveRates(userBaseCurrency);
        var currencyRate = retrieveJsonData(response, userTargetCurrency);
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

    private Double retrieveJsonData(HttpResponse<String> response, String userTargetCurrency) {
        ObjectMapper mapper = new ObjectMapper();
        var currencyRate = 0.0;
        try {
            JsonNode rootNode = mapper.readTree(response.body());
            var nameNode = rootNode.path("conversion_rates").toString();
            TypeReference<HashMap<String, Double>> typeRef
                    = new TypeReference<>() {
            };
            Map<String, Double> map = mapper.readValue(nameNode, typeRef);
            currencyRate = map.entrySet().stream()
                    .filter(element -> userTargetCurrency.equals(element.getKey()))
                    .findFirst()
                    .get()
                    .getValue();
            return currencyRate;
        } catch (IOException e) {
            System.out.println("Invalid currency abbreviation!");
        }
        return currencyRate;
    }
}