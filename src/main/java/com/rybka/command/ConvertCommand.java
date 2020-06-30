package com.rybka.command;

import com.rybka.constant.CommandConstants;
import com.rybka.constant.Messages;
import com.rybka.repository.CurrencyHistoryRepository;
import com.rybka.exception.DBConnectionException;
import com.rybka.exception.IncorrectUserDataException;
import com.rybka.model.CurrencyHistory;
import com.rybka.model.UserConvertData;
import com.rybka.service.exchange.ExchangeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.InputMismatchException;
import java.util.Scanner;

@Component(CommandConstants.CONVERT_COMMAND)
@Slf4j
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
            log.error(Messages.LOG_CONVERSION_ERROR_MSG + exception.getMessage());
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
            log.error(Messages.LOG_CONNECTION_ERROR_MSG + exception.getMessage());
            throw new DBConnectionException(Messages.DB_CONNECTION_EXCEPTION_MSG);
        }
    }

    private UserConvertData readUserParameters() {
        try {
            System.out.print(Messages.INPUT_BASE_MSG);
            var userBaseCurrency = scanner.next().toUpperCase();
            System.out.print(Messages.INPUT_VALUE_MSG);
            var userValue = scanner.nextDouble();
            System.out.print(Messages.INPUT_TARGET_MSG);
            var userTargetCurrency = scanner.next().toUpperCase();

            return new UserConvertData(userBaseCurrency, userTargetCurrency, userValue);

        } catch (InputMismatchException exception) {
            log.error(Messages.LOG_INVALID_VALUE_ERROR_MSG);
            throw new IncorrectUserDataException(Messages.USER_DATA_EXCEPTION_MSG);
        }
    }

    private void showExchange(CurrencyHistory currencyHistory) {
        log.info(String.format(Messages.EXCHANGE_FORMAT, currencyHistory.getAmount(),
                currencyHistory.getBase(), currencyHistory.getTotal(),
                currencyHistory.getTarget()));
    }

    private CurrencyHistory constructConvertedResult(String base, String target, double amount, double rate, double total) {
        return new CurrencyHistory(base, target, amount, rate, total);
    }
}
