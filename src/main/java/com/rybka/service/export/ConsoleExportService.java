package com.rybka.service.export;

import com.rybka.constant.ExportType;
import com.rybka.constant.Messages;
import com.rybka.model.CurrencyHistory;
import com.rybka.util.FormatPrintUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(ExportType.CONSOLE_EXPORT_TYPE)
@Slf4j
public class ConsoleExportService implements ExportService {

    public void export(List<CurrencyHistory> histories) {
        FormatPrintUtil.printHistory(histories);
        log.info(String.format(Messages.LOG_EXPORT_INFO_MSG, ExportType.CONSOLE_EXPORT_TYPE));
    }
}
