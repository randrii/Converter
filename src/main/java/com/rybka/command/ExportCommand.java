package com.rybka.command;

import com.rybka.config.ExportType;
import com.rybka.config.MapSearchUtil;
import com.rybka.config.PropertyInfo;
import com.rybka.dao.CurrencyHistoryDAO;
import com.rybka.service.export.CSVExportService;
import com.rybka.service.export.ConsoleExportService;
import com.rybka.service.export.JSONExportService;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Properties;

@RequiredArgsConstructor
public class ExportCommand implements Command {
    private final Properties reader;
    private final ConsoleExportService consoleExportService;
    private final CSVExportService csvExportService;
    private final JSONExportService jsonExportService;
    private final CurrencyHistoryDAO currencyHistoryDAO;

    @Override
    public void execute() {
        var exportConfigMap = Map.of(
                ExportType.CONSOLE.getType(), consoleExportService,
                ExportType.CSV.getType(), csvExportService,
                ExportType.JSON.getType(), jsonExportService);

        var exportService = MapSearchUtil.retrieveMapValue(exportConfigMap, reader.getProperty(PropertyInfo.PROPERTY_EXPORT_TYPE));
        exportService.export(currencyHistoryDAO.findAll());
    }
}
