package com.rybka.view;

import com.rybka.dao.HibernateDAO;
import com.rybka.model.Currency;
import com.rybka.service.ExchangeService;

import java.util.Scanner;

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

            var currencyObject = service.loadCurrencyOf(userBaseCurrency);
            System.out.println(currencyObject);
            var currency = service.calculateTotal(currencyObject, userTargetCurrency, userValue);
            hibernateDAO.save(currency);
            hibernateDAO.showTableRow();

            showExchange(currency);
        } catch (Exception e) {
            System.out.println("Some issues are occurred! Reason: " + e.getMessage());
        }
    }

    private void showExchange(Currency currency) {
        System.out.println(String.format("%.4f %s -> %.4f %s", currency.getAmount(),
                currency.getBase(), currency.getTotal(),
                currency.getTarget()));
    }

}
