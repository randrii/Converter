package com.rybka;

import com.rybka.command.Command;
import com.rybka.config.CommandConstants;
import com.rybka.view.ExchangeView;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Map;

public class Application {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);

        Map<String, Command> commandMap = Map.of(
                CommandConstants.CONVERT_COMMAND, (Command) context.getBean(CommandConstants.CONVERT_COMMAND),
                CommandConstants.HISTORY_COMMAND, (Command) context.getBean(CommandConstants.HISTORY_COMMAND),
                CommandConstants.EXPORT_COMMAND, (Command) context.getBean(CommandConstants.EXPORT_COMMAND));

        var view = (ExchangeView) context.getBean("exchangeView", commandMap);

        while (true) {
            view.showView();
        }
    }

}
