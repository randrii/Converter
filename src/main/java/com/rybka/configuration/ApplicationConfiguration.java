package com.rybka.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.rybka.constant.*;
import com.rybka.exception.InvalidPropertyException;
import com.rybka.service.connector.BaseCurrencyExchangeConnector;
import com.rybka.util.MapSearchUtil;
import coresearch.cvurl.io.request.CVurl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.net.http.HttpClient;
import java.util.Map;
import java.util.Scanner;

@Configuration
@ComponentScan("com.rybka.*")
@EnableJpaRepositories("com.rybka.repository")
public class ApplicationConfiguration {
    @Autowired
    private Environment environment;

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
        return MapSearchUtil.retrieveMapValue(exchangeSourceMap, environment.getProperty(PropertyInfo.PROPERTY_EXCHANGE_SOURCE), new InvalidPropertyException(Messages.PROPERTY_EXCEPTION_MSG));
    }
}
