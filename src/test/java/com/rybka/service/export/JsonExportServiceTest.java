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
import java.util.Random;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class JsonExportServiceTest {

    @Mock
    private ExportProperty exportProperty;
    @Mock
    private ObjectMapper mapper;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Random random = new Random();
    private JsonExportService jsonExportService;

    @Test
    public void testOnProperExport() {

        // given
        var resourceFolder = "src/test/resources/";
        var tempFolderPath = Paths.get(resourceFolder + random.nextInt() + "/");
        var tempFolder = createTestDirectory(tempFolderPath);
        var exportType = "json";
        var history = buildRandomHistory();
        var testList = List.of(history);
        jsonExportService = new JsonExportService(objectMapper, exportProperty);

        when(exportProperty.getFolder()).thenReturn(tempFolder.toString() + "/");
        when(exportProperty.getType()).thenReturn(exportType);

        // when
        jsonExportService.export(testList);
        Path testFilePath = findSavedFile(tempFolder);
        var actualResult = readSavedFile(testFilePath);

        // then
        verify(exportProperty).getType();
        verify(exportProperty).getFolder();

        Assertions.assertEquals(history, actualResult);

        removeSavedFile(testFilePath);
        removeSavedFile(tempFolderPath);
    }

    @SneakyThrows
    @Test
    public void testOnExportException() {

        // given
        var resourceFolder = "src/test/resources/";
        var exportType = "json";
        var testList = List.of(buildRandomHistory());
        jsonExportService = new JsonExportService(mapper, exportProperty);

        when(exportProperty.getType()).thenReturn(exportType);
        when(exportProperty.getFolder()).thenReturn(resourceFolder);

        // when
        when(mapper.writeValueAsString(any(List.class))).thenThrow(ExportFailureException.class);

        // then
        verifyNoInteractions(exportProperty);
        verifyNoInteractions(exportProperty);

        Assertions.assertThrows(ExportFailureException.class, () -> jsonExportService.export(testList));
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