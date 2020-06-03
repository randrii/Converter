package com.rybka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rybka.dao.HibernateDAO;
import com.rybka.service.ExchangeRateConnector;
import com.rybka.service.ExchangeService;
import com.rybka.service.PrimeExchangeRateConnector;
import com.rybka.view.ExchangeView;
import coresearch.cvurl.io.request.CVurl;

import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.Scanner;

public class Application {
    public static void main(String[] args) {

        var reader = new PropertyReader(
                new HashMap<>() {{
                    put("PrimeExchangeRate", new PrimeExchangeRateConnector(
                            HttpClient.newHttpClient(),
                            new ObjectMapper()));
                    put("ExchangeRate", new ExchangeRateConnector(
                            new CVurl()));
                }});

        var source = reader.getPropertyValue();
        var connector = reader.getObjectOf(source);

        ExchangeView view = new ExchangeView(
                new Scanner(System.in),
                new ExchangeService(connector),
                new HibernateDAO());

        while (true) {
            view.showDialog();
        }
    }
}
