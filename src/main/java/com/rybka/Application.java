package com.rybka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.rybka.command.ConvertCommand;
import com.rybka.command.ExportCommand;
import com.rybka.command.HistoryCommand;
import com.rybka.config.*;
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
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

import java.net.http.HttpClient;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Scanner;

@Service
public class Application {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
        var objectMapper = context.getBean(ObjectMapper.class);

        var primeExchangeRateConnector = context.getBean(PrimeExchangeRateConnector.class, HttpClient.newHttpClient(), objectMapper);
        var exchangeRateConnectorConnector = context.getBean(ExchangeRateConnector.class, context.getBean(CVurl.class));

        var exchangeSourceMap = Map.of(
                ExchangeSource.EXCHANGE.getSource(), exchangeRateConnectorConnector,
                ExchangeSource.PRIME_EXCHANGE.getSource(), primeExchangeRateConnector);

        var reader = context.getBean(PropertyReader.class, PropertyInfo.PROPERTY_FILE_PATH).getProperties();
        var connector = MapSearchUtil.retrieveMapValue(exchangeSourceMap, reader.getProperty(
                PropertyInfo.PROPERTY_EXCHANGE_SOURCE), new InvalidPropertyException("Unsupported export type or exchange source."));

        var exportFolder = reader.getProperty(PropertyInfo.PROPERTY_EXPORT_FOLDER);
        var exportFileName = FileUtils.generateFileName(reader.getProperty(PropertyInfo.PROPERTY_EXPORT_TYPE));

        var consoleExportService = context.getBean(ConsoleExportService.class);

        Path exportPath = Paths.get(exportFolder + exportFileName);

        var csvMapper = context.getBean(CsvMapper.class);

        CSVExportService csvExportService = context.getBean(CSVExportService.class, csvMapper, exportPath);

        JSONExportService jsonExportService = context.getBean(JSONExportService.class, objectMapper, exportPath);

        var exportConfigMap = Map.of(
                ExportType.CONSOLE.getType(), consoleExportService,
                ExportType.CSV.getType(), csvExportService,
                ExportType.JSON.getType(), jsonExportService);

        var currencyHistoryDAO = context.getBean(CurrencyHistoryDAO.class);

        var scanner = context.getBean(Scanner.class, System.in);
        var serviceConnector = context.getBean(ExchangeService.class, connector);

        var convertCommand = context.getBean(ConvertCommand.class, scanner, serviceConnector, currencyHistoryDAO);
        var historyCommand = context.getBean(HistoryCommand.class, currencyHistoryDAO);
        var exportCommand = context.getBean(ExportCommand.class, reader, exportConfigMap, currencyHistoryDAO);

        var commandMap = Map.of(
                CommandConstants.CONVERT_COMMAND, convertCommand,
                CommandConstants.HISTORY_COMMAND, historyCommand,
                CommandConstants.EXPORT_COMMAND, exportCommand);

        ExchangeView view = context.getBean(ExchangeView.class, scanner, commandMap);

        while (true) {
            view.showView();
        }
    }

}
