package com.rybka.command;

import com.rybka.constant.CommandConstants;
import com.rybka.dao.CurrencyHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component(CommandConstants.HISTORY_COMMAND)
@Slf4j
@RequiredArgsConstructor
public class HistoryCommand implements Command {
    private final CurrencyHistoryRepository currencyHistoryRepository;

    @Override
    public void execute() {
        try {
            var histories = currencyHistoryRepository.findTop5ByOrderByIdDesc();
            histories.forEach(System.out::println);
        } catch (Exception exception) {
            log.error("Some issues have been occurred while returning history. Reason: " + exception.getMessage());
        }
    }
}
