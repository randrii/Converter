package com.rybka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.rybka.command.Command;
import com.rybka.command.ConvertCommand;
import com.rybka.command.ExportCommand;
import com.rybka.command.HistoryCommand;
import com.rybka.config.*;
import com.rybka.dao.CurrencyHistoryDAO;
import com.rybka.service.connector.BaseCurrencyExchangeConnector;
import com.rybka.service.connector.ExchangeRateConnector;
import com.rybka.service.connector.PrimeExchangeRateConnector;
import com.rybka.service.exchange.ExchangeService;
import com.rybka.service.export.CSVExportService;
import com.rybka.service.export.ConsoleExportService;
import com.rybka.service.export.ExportService;
import com.rybka.service.export.JSONExportService;
import com.rybka.view.ExchangeView;
import coresearch.cvurl.io.request.CVurl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.net.http.HttpClient;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Scanner;

@Configuration
@ComponentScan("com.rybka")
public class ApplicationConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public PrimeExchangeRateConnector primeExchangeRateConnector() {
        return new PrimeExchangeRateConnector(HttpClient.newHttpClient(), objectMapper());
    }

    @Bean
    public CVurl cVurl() {
        return new CVurl();
    }

    @Bean
    public ExchangeRateConnector exchangeRateConnector() {
        return new ExchangeRateConnector(cVurl());
    }

    @Bean
    public PropertyReader propertyReader() {
        return new PropertyReader(PropertyInfo.PROPERTY_FILE_PATH);
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
    public CSVExportService csvExportService() {
        return new CSVExportService(csvMapper(), exportPath);
    }

    @Bean
    public JSONExportService jsonExportService(Path exportPath) {
        return new JSONExportService(objectMapper(), exportPath);
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
    public ExchangeService exchangeService(PrimeExchangeRateConnector connector) {
        return new ExchangeService(connector);
    }

    @Bean
    public Command convertCommand() {
        return new ConvertCommand(scanner(), exchangeService(primeExchangeRateConnector()), currencyHistoryDAO());
    }

    @Bean
    public Command historyCommand() {
        return new HistoryCommand(currencyHistoryDAO());
    }

    @Bean
    public Command exportCommand(Map<String, ExportService> exportConfigMap) {
        return new ExportCommand(propertyReader().getProperties(), exportConfigMap, currencyHistoryDAO());
    }

    @Bean
    public ExchangeView exchangeView(Map<String, Command> commandMap) {
        return new ExchangeView(scanner(), commandMap);
    }
}
