package com.rybka.command;

import com.rybka.constant.CommandConstants;
import com.rybka.constant.Messages;
import com.rybka.util.MapSearchUtil;
import com.rybka.constant.PropertyInfo;
import com.rybka.repository.CurrencyHistoryRepository;
import com.rybka.exception.InvalidPropertyException;
import com.rybka.service.export.ExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component(CommandConstants.EXPORT_COMMAND)
@RequiredArgsConstructor
public class ExportCommand implements Command {
    private final Environment environment;
    private final Map<String, ExportService> exportConfigMap;
    private final CurrencyHistoryRepository currencyHistoryRepository;

    @Override
    public void execute() {
        var exportService = MapSearchUtil.retrieveMapValue(exportConfigMap,
                environment.getProperty(PropertyInfo.PROPERTY_EXPORT_TYPE),
                new InvalidPropertyException(Messages.PROPERTY_EXCEPTION_MSG));

        exportService.export(currencyHistoryRepository.findAll());
    }
}
