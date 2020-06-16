package com.rybka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.rybka.config.*;
import com.rybka.dao.CurrencyHistoryDAO;
import com.rybka.exception.InvalidPropertyException;
import com.rybka.service.connector.BaseCurrencyExchangeConnector;
import com.rybka.service.connector.ExchangeRateConnector;
import com.rybka.service.connector.PrimeExchangeRateConnector;
import com.rybka.service.exchange.ExchangeService;
import com.rybka.service.export.CSVExportService;
import com.rybka.service.export.ConsoleExportService;
import com.rybka.service.export.ExportService;
import com.rybka.service.export.JSONExportService;
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
    public BaseCurrencyExchangeConnector primeExchangeRateConnector() {
        return new PrimeExchangeRateConnector(HttpClient.newHttpClient(), objectMapper());
    }

    @Bean
    public CVurl cVurl() {
        return new CVurl();
    }

    @Bean
    public BaseCurrencyExchangeConnector exchangeRateConnector() {
        return new ExchangeRateConnector(cVurl());
    }

    @Bean
    public ConsoleExportService consoleExportService() {
        return new ConsoleExportService();
    }

    @Bean
    public CsvMapper csvMapper() {
        return new CsvMapper();
    }

    @Bean
    public Path getExportPath() {
        return Paths.get(environment.getProperty(PropertyInfo.PROPERTY_EXPORT_FOLDER)
                + FileUtils.generateFileName(environment.getProperty(PropertyInfo.PROPERTY_EXPORT_TYPE)));
    }

    @Bean
    public CSVExportService csvExportService() {
        return new CSVExportService(csvMapper(), getExportPath());
    }

    @Bean
    public JSONExportService jsonExportService() {
        return new JSONExportService(objectMapper(), getExportPath());
    }

    @Bean
    public CurrencyHistoryDAO currencyHistoryDAO() {
        return new CurrencyHistoryDAO();
    }

    @Bean
    public Scanner scanner() {
        return new Scanner(System.in);
    }

    @Bean
    public BaseCurrencyExchangeConnector connector() {
        return MapSearchUtil.retrieveMapValue(exchangeSourceMap(), environment.getProperty(
                PropertyInfo.PROPERTY_EXCHANGE_SOURCE), new InvalidPropertyException("Unsupported export type or exchange source."));
    }

    @Bean
    public ExchangeService exchangeService() {
        return new ExchangeService(connector());
    }

    @Bean
    public Map<String, ExportService> exportConfigMap() {
        return Map.of(
                ExportType.CONSOLE.getType(), consoleExportService(),
                ExportType.CSV.getType(), csvExportService(),
                ExportType.JSON.getType(), jsonExportService());
    }

    @Bean
    public Map<String, BaseCurrencyExchangeConnector> exchangeSourceMap() {
        return Map.of(
                ExchangeSource.EXCHANGE.getSource(), exchangeRateConnector(),
                ExchangeSource.PRIME_EXCHANGE.getSource(), primeExchangeRateConnector());
    }
}
