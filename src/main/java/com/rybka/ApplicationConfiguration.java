package com.rybka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.rybka.config.*;
import com.rybka.exception.InvalidPropertyException;
import com.rybka.service.connector.BaseCurrencyExchangeConnector;
import com.rybka.service.exchange.ExchangeService;
import coresearch.cvurl.io.request.CVurl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.net.http.HttpClient;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Scanner;

@Configuration
@ComponentScan("com.rybka.*")
@PropertySource("classpath:application.properties")
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
    public Path exportPath() {
        return Paths.get(environment.getProperty(PropertyInfo.PROPERTY_EXPORT_FOLDER)
                + FileUtils.generateFileName(environment.getProperty(PropertyInfo.PROPERTY_EXPORT_TYPE)));
    }

    @Bean
    public BaseCurrencyExchangeConnector connector(Map<String, BaseCurrencyExchangeConnector> exchangeSourceMap) {
        return MapSearchUtil.retrieveMapValue(exchangeSourceMap, environment.getProperty(PropertyInfo.PROPERTY_EXCHANGE_SOURCE), new InvalidPropertyException("Unsupported export type or exchange source."));
    }

    @Bean
    public Scanner scanner() {
        return new Scanner(System.in);
    }

    @Bean
    public ExchangeService exchangeService(BaseCurrencyExchangeConnector connector) {
        return new ExchangeService(connector);
    }
}
