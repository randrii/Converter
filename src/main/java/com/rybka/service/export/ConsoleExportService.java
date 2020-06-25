package com.rybka.service.export;

import com.rybka.constant.ExportType;
import com.rybka.model.CurrencyHistory;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(ExportType.CONSOLE_EXPORT_TYPE)
@Slf4j
public class ConsoleExportService implements ExportService {

    public void export(List<CurrencyHistory> histories) {
        histories.forEach(System.out::println);
        log.info("Exporting history to console.");
    }
}
