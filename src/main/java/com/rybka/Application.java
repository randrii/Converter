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

import java.net.http.HttpClient;
import java.nio.file.Path;
import java.nio.file.Paths;
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

        var reader = new PropertyReader(PropertyInfo.PROPERTY_FILE_PATH).getProperties();
        var connector = MapSearchUtil.retrieveMapValue(exchangeSourceMap, reader.getProperty(
                PropertyInfo.PROPERTY_EXCHANGE_SOURCE), new InvalidPropertyException("Unsupported export type or exchange source."));

        var exportFolder = reader.getProperty(PropertyInfo.PROPERTY_EXPORT_FOLDER);
        var exportFileName = FileUtils.generateFileName(reader.getProperty(PropertyInfo.PROPERTY_EXPORT_TYPE));

        var consoleExportService = new ConsoleExportService();

        Path exportPath = Paths.get(exportFolder + exportFileName);

        CSVExportService csvExportService = new CSVExportService(new CsvMapper(), exportPath);
        JSONExportService jsonExportService = new JSONExportService(objectMapper, exportPath);

        var exportConfigMap = Map.of(
                ExportType.CONSOLE.getType(), consoleExportService,
                ExportType.CSV.getType(), csvExportService,
                ExportType.JSON.getType(), jsonExportService);

        var currencyHistoryDAO = new CurrencyHistoryDAO();

        var scanner = new Scanner(System.in);
        var serviceConnector = new ExchangeService(connector);

        var convertCommand = new ConvertCommand(scanner, serviceConnector, currencyHistoryDAO);
        var historyCommand = new HistoryCommand(currencyHistoryDAO);
        var exportCommand = new ExportCommand(reader, exportConfigMap, currencyHistoryDAO);

        var commandMap = Map.of(
                CommandConstants.CONVERT_COMMAND, convertCommand,
                CommandConstants.HISTORY_COMMAND, historyCommand,
                CommandConstants.EXPORT_COMMAND, exportCommand);

        ExchangeView view = new ExchangeView(scanner, commandMap);

        while (true) {
            view.showView();
        }
    }

}
