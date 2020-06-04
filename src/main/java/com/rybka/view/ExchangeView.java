package com.rybka.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rybka.dao.HibernateDAO;
import com.rybka.model.CurrencyHistory;
import com.rybka.service.CSVExportService;
import com.rybka.service.ConsoleExportService;
import com.rybka.service.ExchangeService;
import com.rybka.service.JSONExportService;
import lombok.extern.log4j.Log4j;

import java.util.Scanner;

@Log4j
public class ExchangeView {

    private final Scanner scanner;
    private final ExchangeService service;
    private final HibernateDAO hibernateDAO;

    public ExchangeView(Scanner scanner, ExchangeService service, HibernateDAO hibernateDAO) {
        this.scanner = scanner;
        this.service = service;
        this.hibernateDAO = hibernateDAO;
    }

    public void showDialog() {

        try {
            System.out.print("Insert base currency: ");
            var userBaseCurrency = scanner.next().toUpperCase();
            System.out.print("Enter value: ");
            var userValue = scanner.nextDouble();
            System.out.print("Insert target currency: ");
            var userTargetCurrency = scanner.next().toUpperCase();

            int userExportChoice;
            do {
                System.out.print("Choose way to export data (1-Console, 2-CSV file, 3-Json file): ");
                userExportChoice = scanner.nextInt();
            } while (userExportChoice < 1 || userExportChoice > 3);

            var currencyResponse = service.loadCurrencyOf(userBaseCurrency, userTargetCurrency);

            var total = service.calculateTotal(currencyResponse.getRate(), userValue);
            var convertedResult = constructConvertedResult(currencyResponse.getBase(),
                    currencyResponse.getTarget(),
                    userValue,
                    currencyResponse.getRate(),
                    total);

            hibernateDAO.save(convertedResult);
            var history = hibernateDAO.select();

            showExchange(convertedResult);

            switch (userExportChoice) {
                case 1:
                    new ConsoleExportService().export(history);
                    break;
                case 2:
                    new CSVExportService().export(history, "test.csv");
                    break;
                case 3:
                    new JSONExportService(new ObjectMapper()).export(history, "test.json");
                    break;
                default:
                    break;
            }

        } catch (Exception e) {
            log.error("Some issues are occurred! Reason: " + e.getMessage());
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
