package com.rybka;

import com.rybka.dao.HibernateDAO;
import com.rybka.service.BaseCurrencyExchangeConnector;
import com.rybka.service.ExchangeService;
import com.rybka.service.ExchangeRateBaseCurrencyExchangeConnector;
import com.rybka.view.ExchangeView;
import coresearch.cvurl.io.request.CVurl;

import java.util.Scanner;

public class Application {
    public static void main(String[] args) {

        BaseCurrencyExchangeConnector baseCurrencyExchangeConnector = new ExchangeRateBaseCurrencyExchangeConnector(new CVurl());

        ExchangeView view = new ExchangeView(
                new Scanner(System.in),
                new ExchangeService(baseCurrencyExchangeConnector),
                new HibernateDAO());

        while (true) {
            view.showDialog();
        }
    }
}
