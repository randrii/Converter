package com.rybka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rybka.dao.HibernateDAO;
import com.rybka.service.ExchangeRate_ApiConnectorService;
import com.rybka.service.BaseApiConnectorService;
import com.rybka.service.ExchangeService;
import com.rybka.service.ExchangeRatesApiConnectorService;
import com.rybka.view.ExchangeView;

import java.net.http.HttpClient;
import java.util.Scanner;

public class Application {
    public static void main(String[] args) {

//        BaseApiConnectorService apiConnectorService = new ExchangeRate_ApiConnectorService(
//                HttpClient.newHttpClient(),
//                new ObjectMapper());

        BaseApiConnectorService apiConnectorService = new ExchangeRatesApiConnectorService(new ObjectMapper());

        ExchangeView view = new ExchangeView(
                new Scanner(System.in),
                new ExchangeService(apiConnectorService),
                new HibernateDAO());

        while (true) {
            view.showDialog();
        }
    }
}
