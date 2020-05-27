package com.rybka.view;

import com.rybka.service.ExchangeService;

import java.util.Scanner;

public class ExchangeView {

    private final Scanner scanner = new Scanner(System.in);
    private final ExchangeService service = new ExchangeService();

    public void showDialog() {

        try {
            System.out.print("Insert base currency: ");
            String userBaseCurrencyAbbreviation = scanner.next().toUpperCase();
            System.out.print("Enter value: ");
            Double userValue = scanner.nextDouble();
            System.out.print("Insert target currency: ");

            String userTargetCurrencyAbbreviation = scanner.next().toUpperCase();

            service.loadCurrencyOf(userBaseCurrencyAbbreviation, userTargetCurrencyAbbreviation);
            service.exchange(userValue);
            service.getCurrencyObject();

            getResult();
        } catch (Exception e) {
            System.out.println("Incorrect input!");
            System.exit(2);
        }
    }

    public void getResult() {
        System.out.println(String.format("%.4f %s -> %.4f %s", service.getAmount(),
                service.getBaseCurrencyAbbreviation(), service.getTotal(),
                service.getTargetCurrencyAbbreviation()));
    }

}
