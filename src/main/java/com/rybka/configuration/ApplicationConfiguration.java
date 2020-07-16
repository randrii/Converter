package com.rybka.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.rybka.constant.Messages;
import com.rybka.exception.InvalidPropertyException;
import com.rybka.properties.ExchangeProperty;
import com.rybka.service.connector.BaseCurrencyExchangeConnector;
import com.rybka.util.MapSearchUtil;
import coresearch.cvurl.io.request.CVurl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;
import java.util.Map;
import java.util.Scanner;

@Configuration
public class ApplicationConfiguration {
    @Autowired
    private ExchangeProperty exchangeProperty;

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public HttpClient httpClient() {
        return HttpClient.newHttpClient();
    }

    @Bean
    public CVurl cVurl() {
        return new CVurl();
    }

    @Bean
    public CsvMapper csvMapper() {
        return new CsvMapper();
    }

    @Bean
    public Scanner scanner() {
        return new Scanner(System.in);
    }

    @Bean
    public BaseCurrencyExchangeConnector connectorService(Map<String, BaseCurrencyExchangeConnector> exchangeSourceMap) {
        return MapSearchUtil.retrieveMapValue(exchangeSourceMap, exchangeProperty.getSource(), new InvalidPropertyException(Messages.PROPERTY_EXCEPTION_MSG));
    }
}
