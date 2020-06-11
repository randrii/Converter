package com.rybka.command;

import com.rybka.dao.CurrencyHistoryDAO;
import com.rybka.exception.DBConnectionException;
import com.rybka.model.CurrencyHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

import java.util.List;

@Log4j
@RequiredArgsConstructor
public class HistoryCommand implements Command {
    private final CurrencyHistoryDAO currencyHistoryDAO;

    @Override
    public void execute() {
        try {
            var histories = history();
            print(histories);
        } catch (Exception exception) {
            log.error("Some issues have been occurred while returning history. Reason: " + exception.getMessage());
        }
    }

    public List<CurrencyHistory> history() {
        try {
            return currencyHistoryDAO.findFiveRecords();
        } catch (Exception exception) {
            log.error("Unable to connect to DB while performing action. Reason: " + exception.getMessage());
            throw new DBConnectionException("Connection isn't set.");
        }
    }

    public void print(List<CurrencyHistory> histories) {
        histories.forEach(System.out::println);
    }
}
