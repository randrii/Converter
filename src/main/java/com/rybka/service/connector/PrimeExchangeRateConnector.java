package com.rybka.service.connector;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rybka.configuration.ApplicationProperties;
import com.rybka.constant.ExchangeSource;
import com.rybka.constant.Messages;
import com.rybka.exception.CurrencyAPICallException;
import com.rybka.model.ExchangeResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component(ExchangeSource.PRIME_EXCHANGE_SOURCE)
@Slf4j
@AllArgsConstructor
public class PrimeExchangeRateConnector implements BaseCurrencyExchangeConnector {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final ApplicationProperties applicationProperties;

    public ExchangeResponse retrieveRates(String userBaseCurrency) {
        String url = applicationProperties.getSchema() +
                applicationProperties.getHost() + String.format(applicationProperties.getEndpoint(),
                applicationProperties.getToken(), userBaseCurrency);
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();
            var response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            return objectMapper.readValue(response.body(), ExchangeResponse.class);
        } catch (Exception exception) {
            log.error(String.format(Messages.API_CALL_EXCEPTION_MSG, ExchangeSource.PRIME_EXCHANGE_SOURCE) + exception.getMessage());
            throw new CurrencyAPICallException(String.format(Messages.API_CALL_EXCEPTION_MSG, ExchangeSource.PRIME_EXCHANGE_SOURCE) + exception.getMessage());
        }
    }
}
