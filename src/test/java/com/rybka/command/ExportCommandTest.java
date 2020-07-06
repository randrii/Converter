package com.rybka.command;

import com.rybka.configuration.ExportProperty;
import com.rybka.exception.InvalidPropertyException;
import com.rybka.repository.CurrencyHistoryRepository;
import com.rybka.service.export.ConsoleExportService;
import com.rybka.service.export.CsvExportService;
import com.rybka.service.export.ExportService;
import com.rybka.service.export.JsonExportService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class ExportCommandTest {
    @Mock
    private ConsoleExportService consoleExportService;
    @Mock
    private CsvExportService csvExportService;
    @Mock
    private JsonExportService jsonExportService;
    @Mock
    private ExportProperty exportProperty;
    @Mock
    private CurrencyHistoryRepository currencyHistoryRepository;
    private Map<String, ExportService> exportConfigMap;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        exportConfigMap = new HashMap<>();
        exportConfigMap.put("console", consoleExportService);
        exportConfigMap.put("csv", csvExportService);
        exportConfigMap.put("json", jsonExportService);
    }

    @Test
    public void testOnExportExecution() {

        // given
        var exportType = "csvs";
        var command = new ExportCommand(exportProperty, exportConfigMap, currencyHistoryRepository);

        when(currencyHistoryRepository.findAll()).thenReturn(new ArrayList<>());
        when(exportProperty.getType()).thenReturn(exportType);

        // when
        command.execute();

        // then
        verify(currencyHistoryRepository).findAll();
        verify(exportProperty).getType();
    }

    @Test
    public void testOnExportException() {

        // given
        var incorrectExportType = "xml";
        var command = new ExportCommand(exportProperty, exportConfigMap, currencyHistoryRepository);

        when(currencyHistoryRepository.findAll()).thenReturn(new ArrayList<>());
        when(exportProperty.getType()).thenReturn(incorrectExportType);

        // then
        Assertions.assertThrows(InvalidPropertyException.class, command::execute);
    }
}