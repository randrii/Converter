package com.rybka.service;

import com.rybka.exception.NullResponseException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiConnectorService {

    private static final String API_KEY = "4e0bbdc44fcdf05612fa0882";

    public HttpResponse<String> retrieveRates(String userBaseCurrency) throws NullResponseException {
        String url = String.format("https://prime.exchangerate-api.com/v5/%s/latest/%s",
                API_KEY, userBaseCurrency);
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
        throw new NullResponseException("Response is null");
    }
}
