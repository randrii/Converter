package com.rybka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rybka.config.PropertyInfo;
import com.rybka.dao.HibernateDAO;
import com.rybka.service.BaseCurrencyExchangeConnector;
import com.rybka.service.ExchangeRateConnector;
import com.rybka.service.ExchangeService;
import com.rybka.service.PrimeExchangeRateConnector;
import com.rybka.view.ExchangeView;
import coresearch.cvurl.io.request.CVurl;

import java.net.http.HttpClient;
import java.util.Map;
import java.util.Scanner;

public class Application {

    public static void main(String[] args) {

        var objectMapper = new ObjectMapper();

        BaseCurrencyExchangeConnector primeExchangeRateConnector = new PrimeExchangeRateConnector(HttpClient.newHttpClient(), objectMapper);
        BaseCurrencyExchangeConnector exchangeRateConnectorConnector = new ExchangeRateConnector(new CVurl());

        var exchangeSourceMap = Map.of(
                PropertyInfo.PROPERTY_EXCHANGE_KEY, exchangeRateConnectorConnector,
                PropertyInfo.PROPERTY_PRIME_EXCHANGE_KEY, primeExchangeRateConnector);

        var reader = new PropertyReader(PropertyInfo.PROPERTY_FILE_NAME).getProperties();
        var connector = exchangeSourceMap.get(reader.getProperty(PropertyInfo.PROPERTY_EXCHANGE_SOURCE));

        ExchangeView view = new ExchangeView(
                new Scanner(System.in),
                new ExchangeService(connector),
                new HibernateDAO()
        );

        while (true) {
            view.showDialog();
        }
    }

}
