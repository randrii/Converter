package com.rybka.service;

import com.rybka.model.CurrencyHistory;
import lombok.extern.log4j.Log4j;

import java.util.List;

@Log4j
public class ConsoleExportService implements ExportService {

    public void export(List<CurrencyHistory> histories) {
        histories.forEach(System.out::println);
        log.info("Exporting history to console.");
    }
}
