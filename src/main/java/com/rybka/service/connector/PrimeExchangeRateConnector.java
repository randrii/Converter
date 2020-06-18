package com.rybka.service.connector;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rybka.config.CurrencyAPIConstants;
import com.rybka.config.ExchangeSource;
import com.rybka.exception.CurrencyAPICallException;
import com.rybka.model.ExchangeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component(ExchangeSource.PRIME_EXCHANGE_SOURCE)
@Log4j
@RequiredArgsConstructor
public class PrimeExchangeRateConnector implements BaseCurrencyExchangeConnector {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ExchangeResponse retrieveRates(String userBaseCurrency) {
        String url = String.format(CurrencyAPIConstants.PRIME_EXCHANGE_RATE_API_URL,
                CurrencyAPIConstants.PRIME_EXCHANGE_RATE_API_KEY, userBaseCurrency);
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();
            var response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            return objectMapper.readValue(response.body(), ExchangeResponse.class);
        } catch (Exception exception) {
            log.error("Error while calling Prime Exchange Rate API service. Reason: " + exception.getMessage());
            throw new CurrencyAPICallException("An error appears while calling Prime Exchange Rate API. Reason: " + exception.getMessage());
        }
    }
}
