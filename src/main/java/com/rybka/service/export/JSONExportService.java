package com.rybka.service.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rybka.exception.ExportFailureException;
import com.rybka.model.CurrencyHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component
@Log4j
@RequiredArgsConstructor
public class JSONExportService implements ExportService {
    private final ObjectMapper objectMapper;
    private final Path path;

    public void export(List<CurrencyHistory> histories) {
        try {
            Files.write(path, objectMapper.writeValueAsString(histories).getBytes());
            log.info("Exporting history to JSON file");
        } catch (IOException e) {
            log.error("Unable to export data to JSON. Reason: " + e.getMessage());
            throw new ExportFailureException("Unable to export data to JSON. Reason: " + e.getMessage());
        }
    }
}
