package com.rybka.view;

import com.rybka.command.Command;
import com.rybka.constant.CommandConstants;
import com.rybka.constant.Messages;
import com.rybka.exception.InvalidCommandException;
import com.rybka.util.MapSearchUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Scanner;

@Component
@Slf4j
@RequiredArgsConstructor
public class ExchangeView {
    private final Scanner scanner;
    private final Map<String, Command> commandMap;

    public void showMenuOnStartup() {
        System.out.println("Command list:");
        System.out.println("\t" + CommandConstants.CONVERT_COMMAND);
        System.out.println("\t" + CommandConstants.EXPORT_COMMAND);
        System.out.println("\t" + CommandConstants.HISTORY_COMMAND);
    }

    public void showView() {
        System.out.print(Messages.INPUT_OPTION_MSG);
        var userCommand = scanner.next();

        try {
            var command = MapSearchUtil.retrieveMapValue(commandMap, userCommand,
                    new InvalidCommandException(Messages.COMMAND_EXCEPTION_MSG));

            command.execute();
        } catch (Exception exception) {
            log.error(Messages.LOG_COMMAND_ERROR_MSG + exception.getMessage());
        }
    }

}
