package com.rybka.command;

import com.rybka.config.MapSearchUtil;
import com.rybka.config.PropertyInfo;
import com.rybka.dao.CurrencyHistoryDAO;
import com.rybka.exception.InvalidPropertyException;
import com.rybka.service.export.ExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Properties;

@Component
@RequiredArgsConstructor
public class ExportCommand implements Command {
    private final Properties reader;
    private final Map<String, ExportService> exportConfigMap;
    private final CurrencyHistoryDAO currencyHistoryDAO;

    @Override
    public void execute() {
        var exportService = MapSearchUtil.retrieveMapValue(exportConfigMap,
                reader.getProperty(PropertyInfo.PROPERTY_EXPORT_TYPE),
                new InvalidPropertyException("Unsupported export type or exchange source."));

        exportService.export(currencyHistoryDAO.findAll());
    }
}
