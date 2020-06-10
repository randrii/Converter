package com.rybka.command;

import com.rybka.dao.CurrencyHistoryDAO;
import com.rybka.model.CurrencyHistory;
import com.rybka.service.exchange.ExchangeService;
import com.rybka.service.export.ExportService;
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
    private final ExportService exportService;

    private String userBaseCurrency;
    private String userTargetCurrency;
    private double userValue;

    @Override
    public void execute() {
        try {
            System.out.print("Insert base currency: ");
            userBaseCurrency = scanner.next().toUpperCase();
            System.out.print("Enter value: ");
            userValue = scanner.nextDouble();
            System.out.print("Insert target currency: ");
            userTargetCurrency = scanner.next().toUpperCase();

            var convertedResult = convert();
            print(convertedResult);
            save(convertedResult);

            exportService.export(currencyHistoryDAO.findAll());
        } catch (NullPointerException exception) {
            log.error("Invalid currency for exchange.");
        } catch (InputMismatchException exception) {
            log.error("Please enter valid value for exchange.");
        } catch (Exception exception) {
            log.error("Some issues are occurred! Reason: " + exception.getMessage());
        }
    }

    private CurrencyHistory convert() {
        var currencyResponse = service.loadCurrencyOf(userBaseCurrency, userTargetCurrency);
        var total = service.calculateTotal(currencyResponse.getRate(), userValue);

        return constructConvertedResult(currencyResponse.getBase(),
                currencyResponse.getTarget(),
                userValue,
                currencyResponse.getRate(),
                total);
    }

    private void print(CurrencyHistory currencyHistory) {
        showExchange(currencyHistory);
    }

    private void save(CurrencyHistory currencyHistory) {
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
