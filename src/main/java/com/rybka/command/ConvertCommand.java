package com.rybka.command;

import com.rybka.constant.CommandConstants;
import com.rybka.dao.CurrencyHistoryRepository;
import com.rybka.exception.DBConnectionException;
import com.rybka.exception.IncorrectUserDataException;
import com.rybka.model.CurrencyHistory;
import com.rybka.model.UserConvertData;
import com.rybka.service.exchange.ExchangeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;

import java.util.InputMismatchException;
import java.util.Scanner;

@Component(CommandConstants.CONVERT_COMMAND)
@Log4j
@RequiredArgsConstructor
public class ConvertCommand implements Command {
    private final Scanner scanner;
    private final ExchangeService exchangeService;
    private final CurrencyHistoryRepository currencyHistoryRepository;

    @Override
    public void execute() {
        try {
            var userConvertData = readUserParameters();
            var convertedResult = convert(userConvertData);

            print(convertedResult);
            save(convertedResult);
        } catch (Exception exception) {
            log.error("Some issues have been occurred during conversion. Reason: " + exception.getMessage());
        }
    }

    private CurrencyHistory convert(UserConvertData userConvertData) {
        var currencyResponse = exchangeService.loadCurrencyOf(userConvertData.getUserBaseCurrency(), userConvertData.getUserTargetCurrency());
        var total = exchangeService.calculateTotal(currencyResponse.getRate(), userConvertData.getUserValue());

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
        try {
            currencyHistoryRepository.save(currencyHistory);
        } catch (Exception exception) {
            log.error("Unable to connect to DB while performing action. Reason: " + exception.getMessage());
            throw new DBConnectionException("Connection isn't set.");
        }
    }

    private UserConvertData readUserParameters() {
        try {
            System.out.print("Insert base currency: ");
            var userBaseCurrency = scanner.next().toUpperCase();
            System.out.print("Enter value: ");
            var userValue = scanner.nextDouble();
            System.out.print("Insert target currency: ");
            var userTargetCurrency = scanner.next().toUpperCase();

            return new UserConvertData(userBaseCurrency, userTargetCurrency, userValue);

        } catch (InputMismatchException exception) {
            log.error("Please enter valid value for exchange.");
            throw new IncorrectUserDataException("Entered data are invalid.");
        }
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
