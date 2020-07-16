package com.rybka.service.export;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
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

@Component(ExportType.CSV_EXPORT_TYPE)
@Slf4j
@RequiredArgsConstructor
public class CsvExportService implements ExportService {
    private final CsvMapper csvMapper;
    private final ExportProperty exportProperty;

    public void export(List<CurrencyHistory> histories) {
        csvMapper.disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);
        CsvSchema csvSchema = csvMapper.schemaFor(CurrencyHistory.class).withHeader();
        ObjectWriter writer = csvMapper.writer(csvSchema.withLineSeparator("\n"));

        try {
            writer.writeValue(Files.newBufferedWriter(buildExportPath()), histories);
            log.info(String.format(Messages.LOG_EXPORT_INFO_MSG, ExportType.CSV_EXPORT_TYPE));
        } catch (IOException e) {
            log.error(String.format(Messages.EXPORT_EXCEPTION_MSG, ExportType.CSV_EXPORT_TYPE) + e.getMessage());
            throw new ExportFailureException(String.format(Messages.EXPORT_EXCEPTION_MSG, ExportType.CSV_EXPORT_TYPE) + e.getMessage());
        }
    }

    private Path buildExportPath() {
        return Paths.get(exportProperty.getFolder()
                + FileUtils.generateFileName(exportProperty.getType()));
    }

}
