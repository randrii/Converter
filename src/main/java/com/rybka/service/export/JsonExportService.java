package com.rybka.service.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rybka.properties.ExportProperty;
import com.rybka.constant.ExportType;
import com.rybka.constant.Messages;
import com.rybka.util.FileUtils;
import com.rybka.exception.ExportFailureException;
import com.rybka.model.CurrencyHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Component(ExportType.JSON_EXPORT_TYPE)
@Slf4j
@RequiredArgsConstructor
public class JsonExportService implements ExportService {
    private final ObjectMapper objectMapper;
    private final ExportProperty exportProperty;

    public void export(List<CurrencyHistory> histories) {
        try {
            Files.write(buildExportPath(), objectMapper.writeValueAsString(histories).getBytes());
            log.info(String.format(Messages.LOG_EXPORT_INFO_MSG, ExportType.JSON_EXPORT_TYPE));
        } catch (IOException e) {
            log.error(String.format(Messages.EXPORT_EXCEPTION_MSG, ExportType.JSON_EXPORT_TYPE) + e.getMessage());
            throw new ExportFailureException(String.format(Messages.EXPORT_EXCEPTION_MSG, ExportType.JSON_EXPORT_TYPE) + e.getMessage());
        }
    }

    private Path buildExportPath() {
        return Paths.get(exportProperty.getFolder()
                + FileUtils.generateFileName(exportProperty.getType()));
    }
}
