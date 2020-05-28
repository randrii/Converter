package com.rybka.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rybka.exception.CurrencyAPICallException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class ApiConnectorService {

    private static final String API_KEY = "4e0bbdc44fcdf05612fa0882";

    public Map<String, Double> retrieveRates(String userBaseCurrency) throws CurrencyAPICallException {
        String url = String.format("https://prime.exchangerate-api.com/v5/%s/latest/%s",
                API_KEY, userBaseCurrency);
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();
            var response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response.body());
            var nameNode = rootNode.path("conversion_rates").toString();

            TypeReference<HashMap<String, Double>> typeRef = new TypeReference<>() {
            };

            return mapper.readValue(nameNode, typeRef);
        } catch (InterruptedException | IOException interruptedException) {
            interruptedException.printStackTrace();
            throw new CurrencyAPICallException("Response is null!");
        }
    }
}
