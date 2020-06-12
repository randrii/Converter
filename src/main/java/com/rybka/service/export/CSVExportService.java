package com.rybka.service.export;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.rybka.exception.ExportFailureException;
import com.rybka.model.CurrencyHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Log4j
@RequiredArgsConstructor
public class CSVExportService implements ExportService {
    private final CsvMapper csvMapper;
    private final Path path;

    public void export(List<CurrencyHistory> histories) {
        csvMapper.disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);
        CsvSchema csvSchema = csvMapper.schemaFor(CurrencyHistory.class).withHeader();
        ObjectWriter writer = csvMapper.writer(csvSchema.withLineSeparator("\n"));

        try {
            writer.writeValue(Files.newBufferedWriter(path), histories);
            log.info("Exporting history to CSV file.");
        } catch (IOException e) {
            log.error("Unable to export to CSV file. Reason: " + e.getMessage());
            throw new ExportFailureException("Unable to export to CSV file. Reason: " + e.getMessage());
        }
    }

}
