package com.rybka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.rybka.config.ExchangeSource;
import com.rybka.config.ExportType;
import com.rybka.config.FileUtils;
import com.rybka.config.PropertyInfo;
import com.rybka.dao.CurrencyHistoryDAO;
import com.rybka.exception.InvalidPropertyException;
import com.rybka.service.connector.ExchangeRateConnector;
import com.rybka.service.exchange.ExchangeService;
import com.rybka.service.connector.PrimeExchangeRateConnector;
import com.rybka.service.export.CSVExportService;
import com.rybka.service.export.ConsoleExportService;
import com.rybka.service.export.JSONExportService;
import com.rybka.view.ExchangeView;
import coresearch.cvurl.io.request.CVurl;

import java.net.http.HttpClient;
import java.util.Map;
import java.util.Scanner;

public class Application {

    public static void main(String[] args) {

        var objectMapper = new ObjectMapper();

        var primeExchangeRateConnector = new PrimeExchangeRateConnector(HttpClient.newHttpClient(), objectMapper);
        var exchangeRateConnectorConnector = new ExchangeRateConnector(new CVurl());

        var exchangeSourceMap = Map.of(
                ExchangeSource.EXCHANGE.getSource(), exchangeRateConnectorConnector,
                ExchangeSource.PRIME_EXCHANGE.getSource(), primeExchangeRateConnector);

        var reader = new PropertyReader(PropertyInfo.PROPERTY_FILE_NAME).getProperties();
        var connector = exchangeSourceMap.entrySet().stream()
                .filter(item -> reader.getProperty(PropertyInfo.PROPERTY_EXCHANGE_SOURCE).equals(item.getKey()))
                .findAny()
                .orElseThrow(() -> new InvalidPropertyException("Cannot find specified source. Try using another one."))
                .getValue();

        var exportFolder = reader.getProperty(PropertyInfo.PROPERTY_EXPORT_FOLDER);
        var exportFileName = FileUtils.generateFileName(reader.getProperty(PropertyInfo.PROPERTY_EXPORT_TYPE));

        var consoleExportService = new ConsoleExportService();
        var csvExportService = new CSVExportService(new CsvMapper(), exportFolder, exportFileName);
        var jsonExportService = new JSONExportService(objectMapper, exportFolder, exportFileName);

        var exportConfigMap = Map.of(
                ExportType.CONSOLE.getType(), consoleExportService,
                ExportType.CSV.getType(), csvExportService,
                ExportType.JSON.getType(), jsonExportService);

        var exportService = exportConfigMap.entrySet().stream()
                .filter(item -> reader.getProperty(PropertyInfo.PROPERTY_EXPORT_TYPE).equals(item.getKey()))
                .findFirst()
                .orElseThrow(() -> new InvalidPropertyException("Unable to find specified export type or it isn't supported."))
                .getValue();

        ExchangeView view = new ExchangeView(
                new Scanner(System.in),
                new ExchangeService(connector),
                new CurrencyHistoryDAO(),
                exportService
        );

        while (true) {
            view.showDialog();
        }
    }

}
