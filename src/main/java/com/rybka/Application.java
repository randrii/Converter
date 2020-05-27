package com.rybka;

import com.rybka.view.ExchangeView;

public class Application {
    public static void main(String[] args) {
        ExchangeView view = new ExchangeView();
        while (true) {
            view.showDialog();
        }
    }
}
