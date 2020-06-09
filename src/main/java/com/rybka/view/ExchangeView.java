package com.rybka.view;

import com.rybka.dao.CurrencyHistoryDAO;
import com.rybka.model.CurrencyHistory;
import com.rybka.service.exchange.ExchangeService;
import com.rybka.service.export.ExportService;
import lombok.extern.log4j.Log4j;

import java.util.InputMismatchException;
import java.util.Scanner;

@Log4j
public class ExchangeView {

    private final Scanner scanner;
    private final ExchangeService service;
    private final CurrencyHistoryDAO currencyHistoryDAO;
    private final ExportService exportService;

    public ExchangeView(Scanner scanner, ExchangeService service, CurrencyHistoryDAO currencyHistoryDAO, ExportService exportService) {
        this.scanner = scanner;
        this.service = service;
        this.currencyHistoryDAO = currencyHistoryDAO;
        this.exportService = exportService;
    }

    public void showDialog() {

        try {
            System.out.print("Insert base currency: ");
            var userBaseCurrency = scanner.next().toUpperCase();
            System.out.print("Enter value: ");
            var userValue = scanner.nextDouble();
            System.out.print("Insert target currency: ");
            var userTargetCurrency = scanner.next().toUpperCase();

            var currencyResponse = service.loadCurrencyOf(userBaseCurrency, userTargetCurrency);

            var total = service.calculateTotal(currencyResponse.getRate(), userValue);
            var convertedResult = constructConvertedResult(currencyResponse.getBase(),
                    currencyResponse.getTarget(),
                    userValue,
                    currencyResponse.getRate(),
                    total);

            currencyHistoryDAO.save(convertedResult);

            showExchange(convertedResult);

            exportService.export(currencyHistoryDAO.findAll());
        } catch (NullPointerException exception) {
            log.error("Invalid currency for exchange.");
        } catch (InputMismatchException exception) {
            log.error("Please enter valid value for exchange.");
        } catch (Exception exception) {
            log.error("Some issues are occurred! Reason: " + exception.getMessage());
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
