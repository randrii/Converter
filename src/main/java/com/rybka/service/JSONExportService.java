package com.rybka.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rybka.config.FileUtils;
import com.rybka.config.PropertyInfo;
import com.rybka.exception.ExportFailureException;
import com.rybka.model.CurrencyHistory;
import lombok.extern.log4j.Log4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Log4j
public class JSONExportService implements ExportService {
    private final ObjectMapper objectMapper;
    private final FileWriter fileWriter;

    public JSONExportService(ObjectMapper objectMapper, FileWriter fileWriter) {
        this.objectMapper = objectMapper;
        this.fileWriter = fileWriter;
    }

    public void export(List<CurrencyHistory> histories) {
        try (fileWriter) {
            fileWriter.write(objectMapper.writeValueAsString(histories));
            fileWriter.flush();

            log.info("Exporting history to JSON file");
        } catch (IOException e) {
            log.error("Unable to export data to JSON. Reason: " + e.getMessage());
            throw new ExportFailureException("Unable to export data to JSON. Reason: " + e.getMessage());
        }
    }
}
