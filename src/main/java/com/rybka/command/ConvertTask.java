package com.rybka.command;

import com.rybka.dao.CurrencyHistoryDAO;
import com.rybka.model.CurrencyHistory;
import com.rybka.service.exchange.ExchangeService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@AllArgsConstructor
public class ConvertTask {
    private final ExchangeService service;
    private final CurrencyHistoryDAO currencyHistoryDAO;
    private final String userBaseCurrency;
    private final String userTargetCurrency;
    private final double userValue;

    public CurrencyHistory convert() {
        var currencyResponse = service.loadCurrencyOf(userBaseCurrency, userTargetCurrency);

        var total = service.calculateTotal(currencyResponse.getRate(), userValue);
        return constructConvertedResult(currencyResponse.getBase(),
                currencyResponse.getTarget(),
                userValue,
                currencyResponse.getRate(),
                total);
    }

    public void print(CurrencyHistory currencyHistory) {
        showExchange(currencyHistory);
    }

    public void save(CurrencyHistory currencyHistory) {
        currencyHistoryDAO.save(currencyHistory);
    }

    private void showExchange(CurrencyHistory currencyHistory) {
        log.info(String.format("%.4f %s -> %.4f %s", currencyHistory.getAmount(),
                currencyHistory.getBase(), currencyHistory.getTotal(),
                currencyHistory.getTarget()));
    }

    private CurrencyHistory constructConvertedResult(String base, String target, double amount, double rate, double total) {
        return new CurrencyHistory(base, target, amount, rate, total);
    }
}
