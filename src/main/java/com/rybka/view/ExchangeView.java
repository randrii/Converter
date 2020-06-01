package com.rybka.view;

import com.rybka.dao.HibernateDAO;
import com.rybka.model.Currency;
import com.rybka.service.ExchangeService;
import lombok.extern.log4j.Log4j;

import java.util.Scanner;

@Log4j
public class ExchangeView {

    private final Scanner scanner = new Scanner(System.in);
    private final ExchangeService service = new ExchangeService();
    private final HibernateDAO hibernateDAO = new HibernateDAO();

    public void showDialog() {

        try {
            System.out.print("Insert base currency: ");
            var userBaseCurrency = scanner.next().toUpperCase();
            System.out.print("Enter value: ");
            var userValue = scanner.nextDouble();
            System.out.print("Insert target currency: ");

            var userTargetCurrency = scanner.next().toUpperCase();

            var currencyResponse = service.loadCurrencyOf(userBaseCurrency, userTargetCurrency);
            log.info(currencyResponse);

            var total = service.calculateTotal(currencyResponse.getRate(), userValue);
            var convertedResult = constructConvertedResult(currencyResponse.getBase(),
                    currencyResponse.getTarget(),
                    userValue,
                    currencyResponse.getRate(),
                    total);

            hibernateDAO.save(convertedResult);
            hibernateDAO.showTableRow();

            showExchange(convertedResult);
        } catch (Exception e) {
            log.error("Some issues are occurred! Reason: " + e.getMessage());
        }
    }

    private void showExchange(Currency currency) {
        log.info(String.format("%.4f %s -> %.4f %s", currency.getAmount(),
                currency.getBase(), currency.getTotal(),
                currency.getTarget()));
    }

    private Currency constructConvertedResult(String base, String target, double amount, double rate, double total) {
        return new Currency(base, target, amount, rate, total);
    }

}
