package com.rybka;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ExchangeService {

    private static final String API_KEY = "4e0bbdc44fcdf05612fa0882";
    private static final String BASE_CURRENCY_ABBREVIATION = "UAH";

    Double currencyValue;
    String baseCurrencyAbbreviation;
    Double amount;
    Double total;

    public ExchangeService loadCurrencyOf(String currencyAbbreviation) {
        String url_str = String.format("https://prime.exchangerate-api.com/v5/%s/latest/%s",
                API_KEY, BASE_CURRENCY_ABBREVIATION);

        try {
            URL url = new URL(url_str);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
            JsonObject jsonobj = root.getAsJsonObject();

            JsonObject jsonObject = jsonobj.get("conversion_rates").getAsJsonObject();

            currencyValue = jsonObject.get(currencyAbbreviation.toUpperCase()).getAsDouble();
            baseCurrencyAbbreviation = currencyAbbreviation.toUpperCase();
            System.out.println(currencyValue);
        } catch (Exception e) {
            System.out.println("Invalid currency abbreviation!");
        }
        return this;
    }

    public ExchangeService exchange(Double amount) {
        this.amount = amount;
        this.total = amount * currencyValue;
        System.out.println(String.format("%.4f", this.total));
        return this;
    }

    public ExchangeService getCurrencyObject() {
        Currency currency = new Currency(1, baseCurrencyAbbreviation, currencyValue, amount, total);
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(currency);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Currency> currencies = session.createQuery("from Currency", Currency.class).list();
            currencies.forEach(c -> System.out.println(currency.toString()));
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return this;
    }
}
