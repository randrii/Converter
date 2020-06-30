package com.rybka.command;

import com.rybka.configuration.ExportProperty;
import com.rybka.constant.CommandConstants;
import com.rybka.constant.Messages;
import com.rybka.exception.InvalidPropertyException;
import com.rybka.repository.CurrencyHistoryRepository;
import com.rybka.service.export.ExportService;
import com.rybka.util.MapSearchUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component(CommandConstants.EXPORT_COMMAND)
@RequiredArgsConstructor
public class ExportCommand implements Command {
    private final ExportProperty exportProperty;
    private final Map<String, ExportService> exportConfigMap;
    private final CurrencyHistoryRepository currencyHistoryRepository;

    @Override
    public void execute() {
        var exportService = MapSearchUtil.retrieveMapValue(exportConfigMap,
                exportProperty.getType(),
                new InvalidPropertyException(Messages.PROPERTY_EXCEPTION_MSG));

        exportService.export(currencyHistoryRepository.findAll());
    }
}
