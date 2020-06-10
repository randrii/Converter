package com.rybka.command;

import com.rybka.dao.CurrencyHistoryDAO;
import com.rybka.exception.CurrencyAPICallException;
import com.rybka.exception.DBConnectionException;
import com.rybka.model.CurrencyHistory;
import com.rybka.model.UserConvertData;
import com.rybka.service.exchange.ExchangeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

import java.util.InputMismatchException;
import java.util.Scanner;

@Log4j
@RequiredArgsConstructor
public class ConvertCommand implements Command {
    private final Scanner scanner;
    private final ExchangeService service;
    private final CurrencyHistoryDAO currencyHistoryDAO;

    @Override
    public void execute() {
        try {
            System.out.print("Insert base currency: ");
            var userBaseCurrency = scanner.next().toUpperCase();
            System.out.print("Enter value: ");
            var userValue = scanner.nextDouble();
            System.out.print("Insert target currency: ");
            var userTargetCurrency = scanner.next().toUpperCase();

            var userConvertData = readUserParameters(userBaseCurrency, userTargetCurrency, userValue);
            var convertedResult = convert(userConvertData);

            print(convertedResult);
            save(convertedResult);
        } catch (InputMismatchException exception) {
            log.error("Please enter valid value for exchange.");
        } catch (Exception exception) {
            log.error("Some issues are occurred! Reason: " + exception.getMessage());
        }
    }

    private CurrencyHistory convert(UserConvertData userConvertData) {
        if (service == null) throw new CurrencyAPICallException("Unable to call exchange service.");

        var currencyResponse = service.loadCurrencyOf(userConvertData.getUserBaseCurrency(), userConvertData.getUserTargetCurrency());
        var total = service.calculateTotal(currencyResponse.getRate(), userConvertData.getUserValue());

        return constructConvertedResult(currencyResponse.getBase(),
                currencyResponse.getTarget(),
                userConvertData.getUserValue(),
                currencyResponse.getRate(),
                total);
    }

    private void print(CurrencyHistory currencyHistory) {
        showExchange(currencyHistory);
    }

    private void save(CurrencyHistory currencyHistory) {
        if (currencyHistoryDAO == null) throw new DBConnectionException("Unable to connect to Database.");
        currencyHistoryDAO.save(currencyHistory);
    }

    private UserConvertData readUserParameters(String userBaseCurrency, String userTargetCurrency, double userValue) {
        return new UserConvertData(userBaseCurrency, userTargetCurrency, userValue);
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
