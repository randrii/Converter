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
            String userBaseCurrencyAbbreviation = scanner.next().toUpperCase();
            System.out.print("Enter value: ");
            Double userValue = scanner.nextDouble();
            System.out.print("Insert target currency: ");

            String userTargetCurrencyAbbreviation = scanner.next().toUpperCase();

            var currencyObject = service.loadCurrencyOf(userBaseCurrencyAbbreviation, userTargetCurrencyAbbreviation, userValue);
            hibernateDAO.save(currencyObject);
            hibernateDAO.showTableRow();

            getResult(currencyObject);
        } catch (Exception e) {
            System.out.println("Incorrect input!");
            System.exit(2);
        }
    }

    private void getResult(Currency currency) {
        System.out.println(String.format("%.4f %s -> %.4f %s", currency.getAmount(),
                currency.getBaseCurrencyAbbreviation(), currency.getTotal(),
                currency.getTargetCurrencyAbbreviation()));
    }

}
