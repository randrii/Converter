package com.rybka.service.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rybka.exception.ExportFailureException;
import com.rybka.model.CurrencyHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Log4j
@RequiredArgsConstructor
public class JSONExportService implements ExportService {
    private final ObjectMapper objectMapper;
    private final FileWriter fileWriter;

    public void export(List<CurrencyHistory> histories) {
        try {
            fileWriter.write(objectMapper.writeValueAsString(histories));
            fileWriter.flush();

            log.info("Exporting history to JSON file");
        } catch (IOException e) {
            log.error("Unable to export data to JSON. Reason: " + e.getMessage());
            throw new ExportFailureException("Unable to export data to JSON. Reason: " + e.getMessage());
        }
    }
}
