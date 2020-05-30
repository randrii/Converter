package com.rybka.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rybka.exception.CurrencyAPICallException;
import com.rybka.model.ExchangeResponse;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiConnectorService {

    private static final String API_KEY = "4e0bbdc44fcdf05612fa0882";
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public ExchangeResponse retrieveRates(String userBaseCurrency) {
        String url = String.format("https://prime.exchangerate-api.com/v5/%s/latest/%s",
                API_KEY, userBaseCurrency);
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();
            var response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            JsonNode tree = mapper.readTree(response.body());

            return mapper.treeToValue(tree, ExchangeResponse.class);
        } catch (Exception exception) {
            System.out.println("Error while calling Currency API service. Reason: " + exception.getMessage());
            throw new CurrencyAPICallException();
        }
    }
}
