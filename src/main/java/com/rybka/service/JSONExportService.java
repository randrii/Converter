package com.rybka.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rybka.config.PropertyInfo;
import com.rybka.exception.ExportFailureException;
import com.rybka.model.CurrencyHistory;
import lombok.extern.log4j.Log4j;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Log4j
public class JSONExportService implements ExportService {
    private final ObjectMapper objectMapper;

    public JSONExportService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void export(List<CurrencyHistory> histories) {
        try (var fileWriter = new FileWriter(PropertyInfo.RESOURCE_PATH
                + new RandomNameService().generateFileName()
                + PropertyInfo.JSON_SUFFIX)) {
            fileWriter.write(objectMapper.writeValueAsString(histories));
            fileWriter.flush();

            log.info("Exporting history to JSON file");
        } catch (IOException e) {
            log.error("Unable to export data to JSON. Reason: " + e.getMessage());
            throw new ExportFailureException("Unable to export data to JSON. Reason: " + e.getMessage());
        }
    }
}
