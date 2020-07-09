package com.rybka.service.export;

import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.rybka.configuration.ExportProperty;
import com.rybka.exception.ExportFailureException;
import com.rybka.model.CurrencyHistory;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class CsvExportServiceTest {

    @Mock
    private ExportProperty exportProperty;
    @Mock
    private CsvMapper mapper;
    private final CsvMapper csvMapper = new CsvMapper();
    private final Random random = new Random();
    private CsvExportService csvExportService;

    @Test
    public void testOnProperExport() {

        // given
        var resourceFolder = "src/test/resources/";
        var tempFolderPath = Paths.get(resourceFolder + random.nextInt() + "/");
        var tempFolder = createTestDirectory(tempFolderPath);
        var exportType = "csv";
        var historyList = List.of(buildRandomHistory(), buildRandomHistory());

        csvExportService = new CsvExportService(csvMapper, exportProperty);

        when(exportProperty.getFolder()).thenReturn(tempFolder.toString() + "/");
        when(exportProperty.getType()).thenReturn(exportType);

        // when
        csvExportService.export(historyList);
        var testFilePath = findSavedFile(tempFolder);
        var actualResult = readSavedFile(testFilePath);

        // then
        verify(exportProperty).getType();
        verify(exportProperty).getFolder();

        Assertions.assertEquals(historyList.size(), actualResult.size());

        removeSavedFile(testFilePath);
        removeSavedFile(tempFolderPath);
    }

    @SneakyThrows
    @Test
    public void testOnExportException() {

        // given
        var resourceFolder = "src/test/resources/";
        var tempFolderPath = Paths.get(resourceFolder + random.nextInt() + "/");
        var tempFolder = createTestDirectory(tempFolderPath);
        var exportType = "csv";
        var historyList = List.of(buildRandomHistory());
        var csvSchema = CsvSchema.emptySchema();

        csvExportService = new CsvExportService(mapper, exportProperty);

        when(exportProperty.getFolder()).thenReturn(tempFolder.toString() + "/");
        when(exportProperty.getType()).thenReturn(exportType);
        when(mapper.schemaFor(eq(CurrencyHistory.class))).thenReturn(csvSchema);
        when(mapper.writer(any(CsvSchema.class))).thenReturn(csvMapper.writer());

        // when
        doThrow(ExportFailureException.class).when(mapper).writeValue(any(BufferedWriter.class), any(List.class));

        // then
        verifyNoInteractions(exportProperty);
        verifyNoInteractions(exportProperty);

        Assertions.assertThrows(ExportFailureException.class, () -> csvExportService.export(historyList));

        var testFilePath = findSavedFile(tempFolder);

        removeSavedFile(testFilePath);
        removeSavedFile(tempFolderPath);
    }

    private CurrencyHistory buildRandomHistory() {
        var currencyList = List.of("USD", "EUR", "PLN", "CAD", "UAH");
        var currencyBase = currencyList.get(random.nextInt(currencyList.size()));
        var currencyTarget = currencyList.get(random.nextInt(currencyList.size()));
        var currencyRate = random.nextDouble();
        var currencyAmount = random.nextInt();
        var total = currencyRate * currencyAmount;
        var history = new CurrencyHistory(currencyBase, currencyTarget, currencyAmount, currencyRate, total);
        history.setDate(Calendar.getInstance().getTime());

        return history;
    }

    @SneakyThrows
    private Path createTestDirectory(Path path) {
        return Files.createDirectory(path);
    }

    @SneakyThrows
    private Path findSavedFile(Path path) {
        return Files.list(path).findFirst().get();
    }

    @SneakyThrows
    private List<CurrencyHistory> readSavedFile(Path path) {
        CsvSchema csvSchema = csvMapper.schemaFor(CurrencyHistory.class).withSkipFirstDataRow(true).withLineSeparator("\n");
        ObjectReader reader = csvMapper.readerFor(CurrencyHistory.class).with(csvSchema);
        List<CurrencyHistory> historyList = new ArrayList<>();
        var iterator = reader.readValues(path.toFile());

        while (iterator.hasNextValue()) {
            historyList.add((CurrencyHistory) iterator.next());
        }
        return historyList;
    }

    @SneakyThrows
    private void removeSavedFile(Path path) {
        Files.deleteIfExists(path);
    }
}