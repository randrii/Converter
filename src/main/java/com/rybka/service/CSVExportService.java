package com.rybka.service;

import com.opencsv.CSVWriter;
import com.rybka.model.CurrencyHistory;
import lombok.extern.log4j.Log4j;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Log4j
public class CSVExportService {

    public void export(List<CurrencyHistory> histories, String file) {
        try (var fileWriter = new FileWriter(file); var csvWriter = new CSVWriter(fileWriter)) {
            histories.stream()
                    .map(CurrencyHistory::toString)
                    .forEach(item -> csvWriter.writeNext(item.split("\n")));
        } catch (IOException e) {
            log.error("Unable to export to CSV file. Reason: " + e.getMessage());
        }
    }

}
