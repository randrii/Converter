package com.rybka.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rybka.CurrencyAPIConstants;
import com.rybka.exception.CurrencyAPICallException;
import com.rybka.model.ExchangeResponse;
import lombok.extern.log4j.Log4j;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Log4j
public class ApiConnectorService {

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public ExchangeResponse retrieveRates(String userBaseCurrency) {
        String url = String.format(CurrencyAPIConstants.API_URL,
                CurrencyAPIConstants.API_KEY, userBaseCurrency);
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();
            var response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            return mapper.readValue(response.body(), ExchangeResponse.class);
        } catch (Exception exception) {
            log.error("Error while calling Currency API service. Reason: " + exception.getMessage());
            throw new CurrencyAPICallException("Wrong parameters in Currency API request! Reason: " + exception.getMessage());
        }
    }
}
