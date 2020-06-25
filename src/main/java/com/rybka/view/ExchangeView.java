package com.rybka.view;

import com.rybka.command.Command;
import com.rybka.constant.MapSearchUtil;
import com.rybka.exception.InvalidCommandException;
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

    public void showView() {
        System.out.print("Type option here: ");
        var userCommand = scanner.next();

        try {
            var command = MapSearchUtil.retrieveMapValue(commandMap, userCommand,
                    new InvalidCommandException("Incorrect command to implement."));

            command.execute();
        } catch (Exception exception) {
            log.error("Unsupported or invalid input. Reason: " + exception.getMessage());
        }
    }

}
