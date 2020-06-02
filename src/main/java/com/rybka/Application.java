package com.rybka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rybka.dao.HibernateDAO;
import com.rybka.service.ApiConnectorService;
import com.rybka.service.ExchangeService;
import com.rybka.view.ExchangeView;

import java.net.http.HttpClient;
import java.util.Scanner;

public class Application {
    public static void main(String[] args) {
        ExchangeView view = new ExchangeView(
                new Scanner(System.in),
                new ExchangeService(
                        new ApiConnectorService(HttpClient.newHttpClient(),
                                new ObjectMapper())),
                new HibernateDAO());
        while (true) {
            view.showDialog();
        }
    }
}
