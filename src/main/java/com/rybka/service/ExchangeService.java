package com.rybka.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rybka.model.Currency;
import lombok.Data;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Data
public class ExchangeService {

    private static final String API_KEY = "4e0bbdc44fcdf05612fa0882";

    private Currency currency = new Currency();

    public Currency loadCurrencyOf(String userBaseCurrencyAbbreviation, String userTargetCurrencyAbbreviation,
                                   Double amount) {
        var response = sendRequest(userBaseCurrencyAbbreviation);
        var currencyRate = retrieveJsonData(response, userTargetCurrencyAbbreviation);
        if (currencyRate == 0.0) {
            System.out.println("Invalid currency abbreviation");
            System.exit(3);
        }
        return constructDataObject(currencyRate, userBaseCurrencyAbbreviation, userTargetCurrencyAbbreviation, amount);
    }

    private Currency constructDataObject(Double currencyRate, String userBaseCurrencyAbbreviation,
                                         String userTargetCurrencyAbbreviation, Double amount) {
        currency.setCurrencyValue(currencyRate);
        currency.setBaseCurrencyAbbreviation(userBaseCurrencyAbbreviation);
        currency.setTargetCurrencyAbbreviation(userTargetCurrencyAbbreviation);
        currency.setAmount(amount);
        currency.setTotal(currency.getAmount() * currency.getCurrencyValue());
        return currency;
    }

    private HttpResponse<String> sendRequest(String userBaseCurrencyAbbreviation) {
        String url = String.format("https://prime.exchangerate-api.com/v5/%s/latest/%s",
                API_KEY, userBaseCurrencyAbbreviation);
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            return client.send(request,
                    HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException | IOException interruptedException) {
            interruptedException.printStackTrace();
        }
        return null;
    }

    private Double retrieveJsonData(HttpResponse<String> response, String userTargetCurrencyAbbreviation) {
        ObjectMapper mapper = new ObjectMapper();
        var currencyRate = 0.0;
        try {
            JsonNode rootNode = mapper.readTree(response.body());
            var nameNode = rootNode.path("conversion_rates");
            currencyRate = nameNode.path(userTargetCurrencyAbbreviation).doubleValue();
            return currencyRate;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return currencyRate;
    }
}