package com.rybka.service.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rybka.configuration.ExportProperty;
import com.rybka.exception.ExportFailureException;
import com.rybka.model.CurrencyHistory;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class JsonExportServiceTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private ExportProperty exportProperty;

    @Test
    public void testOnProperExport() {

        // given
        var testFolder = "src/test/resources/";
        var testType = "json";
        var currencyBase = "USD";
        var currencyTarget = "EUR";
        var currencyRate = 0.88;
        var currencyAmount = 4d;
        var currencyTotal = currencyRate * currencyAmount;
        var currencyHistory = new CurrencyHistory(currencyBase, currencyTarget, currencyAmount, currencyRate, currencyTotal);
        currencyHistory.setDate(Calendar.getInstance().getTime());
        var testList = List.of(currencyHistory);
        var jsonExportService = new JsonExportService(objectMapper, exportProperty);

        when(exportProperty.getFolder()).thenReturn(testFolder);
        when(exportProperty.getType()).thenReturn(testType);

        // when
        jsonExportService.export(testList);
        Path testFilePath = findSavedFile(testFolder, testType);
        var actualResult = readSavedFile(testFilePath);

        // then
        verify(exportProperty).getType();
        verify(exportProperty).getFolder();

        Assertions.assertEquals(currencyRate, actualResult.getRate());

        removeSavedFile(testFilePath);
    }

    @Test
    public void testOnExportException() {

        // given
        var testType = "json";
        var currencyBase = "USD";
        var currencyTarget = "EUR";
        var currencyRate = 0.88;
        var currencyAmount = 4d;
        var currencyTotal = currencyRate * currencyAmount;
        var currencyHistory = new CurrencyHistory(currencyBase, currencyTarget, currencyAmount, currencyRate, currencyTotal);
        currencyHistory.setDate(Calendar.getInstance().getTime());
        var testList = List.of(currencyHistory);
        var jsonExportService = new JsonExportService(objectMapper, exportProperty);

        when(exportProperty.getFolder()).thenThrow(ExportFailureException.class);
        when(exportProperty.getType()).thenReturn(testType);

        // then
        verifyNoInteractions(exportProperty);
        verifyNoInteractions(exportProperty);

        Assertions.assertThrows(ExportFailureException.class, () -> jsonExportService.export(testList));
    }

    @SneakyThrows
    private Path findSavedFile(String testFolder, String testType) {
        var testFolderPath = Paths.get(testFolder);
        var matches = Files.find(testFolderPath, 2, ((path, basicFileAttributes) -> path.getFileName().toString().endsWith(testType)));

        return Path.of(testFolder, matches.map(Path::getFileName).findFirst().get().toString());
    }

    @SneakyThrows
    private CurrencyHistory readSavedFile(Path path) {
        var actualResultFile = Files.readString(path);
        var actualResult = actualResultFile.replace("[", "").replace("]", "");

        return objectMapper.readValue(actualResult, CurrencyHistory.class);
    }

    @SneakyThrows
    private void removeSavedFile(Path path) {
        Files.deleteIfExists(path);
    }
}