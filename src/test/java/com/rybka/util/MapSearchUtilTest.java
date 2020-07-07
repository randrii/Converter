package com.rybka.util;

import com.rybka.command.ConvertCommand;
import com.rybka.command.ExportCommand;
import com.rybka.command.HistoryCommand;
import com.rybka.constant.Messages;
import com.rybka.exception.InvalidCommandException;
import com.rybka.exception.InvalidPropertyException;
import com.rybka.service.connector.ExchangeRateConnector;
import com.rybka.service.connector.PrimeExchangeRateConnector;
import com.rybka.service.export.ConsoleExportService;
import com.rybka.service.export.CsvExportService;
import com.rybka.service.export.JsonExportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MapSearchUtilTest {
    @Mock
    private ConsoleExportService consoleExportService;
    @Mock
    private CsvExportService csvExportService;
    @Mock
    private JsonExportService jsonExportService;
    @Mock
    private ExchangeRateConnector exchangeRateConnector;
    @Mock
    private PrimeExchangeRateConnector primeExchangeRateConnector;
    @Mock
    private ConvertCommand convertCommand;
    @Mock
    private HistoryCommand historyCommand;
    @Mock
    private ExportCommand exportCommand;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    // correct tests

    @Test
    public void testOnCorrectExportServiceChoice() {

        // given
        var testExportType = "csv";
        var exportConfigMap = Map.of(
                "console", consoleExportService,
                "csv", csvExportService,
                "json", jsonExportService);

        // then
        assertEquals(csvExportService, MapSearchUtil.retrieveMapValue(exportConfigMap, testExportType, new InvalidPropertyException(Messages.PROPERTY_EXCEPTION_MSG)));
    }

    @Test
    public void testOnCorrectExchangeSourceChoice() {

        // given
        var testExchangeSource = "exchange";
        var exchangeSourceMap = Map.of(
                "exchange", exchangeRateConnector,
                "prime_exchange", primeExchangeRateConnector);

        // then
        assertEquals(exchangeRateConnector, MapSearchUtil.retrieveMapValue(exchangeSourceMap, testExchangeSource, new InvalidPropertyException(Messages.PROPERTY_EXCEPTION_MSG)));
    }

    @Test
    public void testOnCorrectCommandChoice() {

        // given
        var testCommand = "/convert";
        var commandMap = Map.of(
                "/convert", convertCommand,
                "/history", historyCommand,
                "/export", exportCommand);

        // then
        assertEquals(convertCommand, MapSearchUtil.retrieveMapValue(commandMap, testCommand, new InvalidCommandException(Messages.COMMAND_EXCEPTION_MSG)));
    }

    // incorrect tests

    @Test
    public void testOnNullExportServiceChoice() {

        // given
        String testExportType = null;
        var exportConfigMap = Map.of(
                "console", consoleExportService,
                "csv", csvExportService,
                "json", jsonExportService);

        // then
        assertThrows(NullPointerException.class, () -> MapSearchUtil.retrieveMapValue(exportConfigMap, testExportType, new InvalidPropertyException(Messages.PROPERTY_EXCEPTION_MSG)));
    }

    @Test
    public void testOnNullExchangeSourceChoice() {

        // given
        String testExchangeSource = null;
        var exchangeSourceMap = Map.of(
                "exchange", exchangeRateConnector,
                "prime_exchange", primeExchangeRateConnector);

        // then
        assertThrows(NullPointerException.class, () -> MapSearchUtil.retrieveMapValue(exchangeSourceMap, testExchangeSource, new InvalidPropertyException(Messages.PROPERTY_EXCEPTION_MSG)));
    }

    @Test
    public void testOnIncorrectCommandChoice() {

        // given
        String testCommand = null;
        var commandMap = Map.of(
                "/convert", convertCommand,
                "/history", historyCommand,
                "/export", exportCommand);

        // then
        assertThrows(NullPointerException.class, () -> MapSearchUtil.retrieveMapValue(commandMap, testCommand, new InvalidCommandException(Messages.COMMAND_EXCEPTION_MSG)));
    }

    // exception tests

    @Test
    public void testOnExportServiceException() {

        // given
        var testExportType = "xml";
        var exportConfigMap = Map.of(
                "console", consoleExportService,
                "csv", csvExportService,
                "json", jsonExportService);

        // then
        assertThrows(InvalidPropertyException.class, () -> MapSearchUtil.retrieveMapValue(exportConfigMap, testExportType, new InvalidPropertyException(Messages.PROPERTY_EXCEPTION_MSG)));
    }

    @Test
    public void testOnExchangeSourceException() {

        // given
        var testExchangeSource = "luxury_exchange";
        var exchangeSourceMap = Map.of(
                "exchange", exchangeRateConnector,
                "prime_exchange", primeExchangeRateConnector);

        // then
        assertThrows(InvalidPropertyException.class, () -> MapSearchUtil.retrieveMapValue(exchangeSourceMap, testExchangeSource, new InvalidPropertyException(Messages.PROPERTY_EXCEPTION_MSG)));
    }

    @Test
    public void testOnCommandException() {

        // given
        var testCommand = "/test";
        var commandMap = Map.of(
                "/convert", convertCommand,
                "/history", historyCommand,
                "/export", exportCommand);

        // then
        assertThrows(InvalidCommandException.class, () -> MapSearchUtil.retrieveMapValue(commandMap, testCommand, new InvalidCommandException(Messages.COMMAND_EXCEPTION_MSG)));
    }
}