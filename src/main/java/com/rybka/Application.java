package com.rybka;

import com.rybka.command.Command;
import com.rybka.config.*;
import com.rybka.service.connector.BaseCurrencyExchangeConnector;
import com.rybka.service.export.ExportService;
import com.rybka.view.ExchangeView;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Map;

public class Application {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);

        Map<String, BaseCurrencyExchangeConnector> exchangeSourceMap = Map.of(
                ExchangeSource.EXCHANGE_SOURCE, (BaseCurrencyExchangeConnector) context.getBean(ExchangeSource.EXCHANGE_SOURCE),
                ExchangeSource.PRIME_EXCHANGE_SOURCE, (BaseCurrencyExchangeConnector) context.getBean(ExchangeSource.PRIME_EXCHANGE_SOURCE));

        Map<String, ExportService> exportConfigMap = Map.of(
                ExportType.CONSOLE_EXPORT_TYPE, (ExportService) context.getBean(ExportType.CONSOLE_EXPORT_TYPE),
                ExportType.CSV_EXPORT_TYPE, (ExportService) context.getBean(ExportType.CSV_EXPORT_TYPE),
                ExportType.JSON_EXPORT_TYPE, (ExportService) context.getBean(ExportType.JSON_EXPORT_TYPE));

        var serviceConnector = context.getBean("exchangeService", context.getBean("connector", exchangeSourceMap));

        Map<String, Command> commandMap = Map.of(
                CommandConstants.CONVERT_COMMAND, (Command) context.getBean(CommandConstants.CONVERT_COMMAND, serviceConnector),
                CommandConstants.HISTORY_COMMAND, (Command) context.getBean(CommandConstants.HISTORY_COMMAND),
                CommandConstants.EXPORT_COMMAND, (Command) context.getBean(CommandConstants.EXPORT_COMMAND, exportConfigMap));

        var view = (ExchangeView) context.getBean("exchangeView", commandMap);

        while (true) {
            view.showView();
        }
    }

}
