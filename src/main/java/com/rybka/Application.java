package com.rybka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.rybka.command.ConvertCommand;
import com.rybka.config.*;
import com.rybka.dao.CurrencyHistoryDAO;
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
        var connector = MapSearchUtil.retrieveMapValue(exchangeSourceMap, reader.getProperty(
                PropertyInfo.PROPERTY_EXCHANGE_SOURCE));

        var exportFolder = reader.getProperty(PropertyInfo.PROPERTY_EXPORT_FOLDER);
        var exportFileName = FileUtils.generateFileName(reader.getProperty(PropertyInfo.PROPERTY_EXPORT_TYPE));

        var consoleExportService = new ConsoleExportService();
        var csvExportService = new CSVExportService(new CsvMapper(), exportFolder, exportFileName);
        var jsonExportService = new JSONExportService(objectMapper, exportFolder, exportFileName);

        var exportConfigMap = Map.of(
                ExportType.CONSOLE.getType(), consoleExportService,
                ExportType.CSV.getType(), csvExportService,
                ExportType.JSON.getType(), jsonExportService);

        var exportService = MapSearchUtil.retrieveMapValue(exportConfigMap, reader.getProperty(
                PropertyInfo.PROPERTY_EXPORT_TYPE));

        ExchangeView view = new ExchangeView(
                new ConvertCommand(
                        new Scanner(System.in),
                        new ExchangeService(connector),
                        new CurrencyHistoryDAO(),
                        exportService
                )
        );

        while (true) {
            view.showView();
        }
    }

}
