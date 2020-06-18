package com.rybka.command;

import com.rybka.config.CommandConstants;
import com.rybka.config.QueryConstants;
import com.rybka.dao.CurrencyHistoryDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;

@Component(CommandConstants.HISTORY_COMMAND)
@Log4j
@RequiredArgsConstructor
public class HistoryCommand implements Command {
    private final CurrencyHistoryDAO currencyHistoryDAO;

    @Override
    public void execute() {
        try {
            var histories = currencyHistoryDAO.findAllLimit(QueryConstants.LIMIT_ROW_NUMBER);
            histories.forEach(System.out::println);
        } catch (Exception exception) {
            log.error("Some issues have been occurred while returning history. Reason: " + exception.getMessage());
        }
    }
}
