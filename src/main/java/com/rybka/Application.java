package com.rybka;

public class Application {
    public static void main(String[] args) {
        ExchangeService service = new ExchangeService();
        service.loadCurrencyOf("USD");
        service.exchange(100.0);
        service.getCurrencyObject();
    }
}
