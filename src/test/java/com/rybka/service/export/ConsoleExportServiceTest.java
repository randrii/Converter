package com.rybka.service.export;

import com.rybka.model.CurrencyHistory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConsoleExportServiceTest {

    private final ConsoleExportService service = new ConsoleExportService();

    @Test
    public void testOnProperExport() {

        //given
        List<CurrencyHistory> testList = List.of();

        // then
        assertDoesNotThrow(() -> service.export(testList));
    }

    @Test
    public void testOnExportException() {

        //given
        List<CurrencyHistory> testList = null;

        // then
        assertThrows(NullPointerException.class, () -> service.export(testList));
    }
}