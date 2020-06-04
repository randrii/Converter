package com.rybka.view;

import com.rybka.dao.HibernateDAO;
import com.rybka.model.CurrencyHistory;
import com.rybka.service.ExchangeService;
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

            var currencyResponse = service.loadCurrencyOf(userBaseCurrency, userTargetCurrency);

            var total = service.calculateTotal(currencyResponse.getRate(), userValue);
            var convertedResult = constructConvertedResult(currencyResponse.getBase(),
                    currencyResponse.getTarget(),
                    userValue,
                    currencyResponse.getRate(),
                    total);

            hibernateDAO.save(convertedResult);

            showExchange(convertedResult);

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
