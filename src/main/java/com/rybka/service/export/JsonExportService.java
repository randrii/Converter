package com.rybka.service.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rybka.config.ExportType;
import com.rybka.config.FileUtils;
import com.rybka.config.PropertyInfo;
import com.rybka.exception.ExportFailureException;
import com.rybka.model.CurrencyHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Component(ExportType.JSON_EXPORT_TYPE)
@Log4j
@RequiredArgsConstructor
public class JsonExportService implements ExportService {
    private final ObjectMapper objectMapper;
    private final Environment environment;

    public void export(List<CurrencyHistory> histories) {
        try {
            Files.write(buildExportPath(), objectMapper.writeValueAsString(histories).getBytes());
            log.info("Exporting history to JSON file");
        } catch (IOException e) {
            log.error("Unable to export data to JSON. Reason: " + e.getMessage());
            throw new ExportFailureException("Unable to export data to JSON. Reason: " + e.getMessage());
        }
    }

    private Path buildExportPath() {
        return Paths.get(environment.getProperty(PropertyInfo.PROPERTY_EXPORT_FOLDER)
                + FileUtils.generateFileName(environment.getProperty(PropertyInfo.PROPERTY_EXPORT_TYPE)));
    }
}
