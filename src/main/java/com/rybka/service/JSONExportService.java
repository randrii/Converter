package com.rybka.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rybka.model.CurrencyHistory;
import lombok.extern.log4j.Log4j;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Log4j
public class JSONExportService {
    private final ObjectMapper objectMapper;

    public JSONExportService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void export(List<CurrencyHistory> histories, String fileName) {
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(objectMapper.writeValueAsString(histories));
            fileWriter.flush();
        } catch (IOException e) {
            log.error("Unable to export data to JSON. Reason: " + e.getMessage());
        }
    }
}
