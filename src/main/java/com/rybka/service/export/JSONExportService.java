package com.rybka.service.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rybka.exception.ExportFailureException;
import com.rybka.model.CurrencyHistory;
import lombok.extern.log4j.Log4j;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Log4j
public class JSONExportService implements ExportService {
    private final ObjectMapper objectMapper;
    private final String folder;
    private final String fileName;

    public JSONExportService(ObjectMapper objectMapper, String folder, String fileName) {
        this.objectMapper = objectMapper;
        this.folder = folder;
        this.fileName = fileName;
    }

    public void export(List<CurrencyHistory> histories) {
        try (var fileWriter = new FileWriter(folder + fileName)) {
            fileWriter.write(objectMapper.writeValueAsString(histories));
            fileWriter.flush();

            log.info("Exporting history to JSON file");
        } catch (IOException e) {
            log.error("Unable to export data to JSON. Reason: " + e.getMessage());
            throw new ExportFailureException("Unable to export data to JSON. Reason: " + e.getMessage());
        }
    }
}
