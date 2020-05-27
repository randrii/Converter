package com.rybka.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rybka.dao.HibernateDAO;
import com.rybka.model.Currency;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ExchangeService {

    private static final String API_KEY = "4e0bbdc44fcdf05612fa0882";

    private Double currencyValue;
    private String baseCurrencyAbbreviation;
    private String targetCurrencyAbbreviation;
    private Double amount;
    private Double total;

    public void loadCurrencyOf(String userBaseCurrencyAbbreviation, String userTargetCurrencyAbbreviation) {
        String url_str = String.format("https://prime.exchangerate-api.com/v5/%s/latest/%s",
                API_KEY, userBaseCurrencyAbbreviation);

        try {
            URL url = new URL(url_str);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
            JsonObject jsonobj = root.getAsJsonObject();

            JsonObject jsonObject = jsonobj.get("conversion_rates").getAsJsonObject();

            currencyValue = jsonObject.get(userTargetCurrencyAbbreviation).getAsDouble();
            baseCurrencyAbbreviation = userBaseCurrencyAbbreviation;
            targetCurrencyAbbreviation = userTargetCurrencyAbbreviation;
        } catch (Exception e) {
            System.out.println("Incorrect exchange data!");
            System.exit(3);
        }
    }

    public void exchange(Double amount) {

        this.amount = amount;
        this.total = amount * currencyValue;
    }

    public void getCurrencyObject() {
        Currency currency = new Currency(baseCurrencyAbbreviation, targetCurrencyAbbreviation, currencyValue, amount, total);
        HibernateDAO hibernateDAO = new HibernateDAO();
        hibernateDAO.save(currency);
        hibernateDAO.showTableRow();
    }

    public Double getCurrencyValue() {
        return currencyValue;
    }

    public void setCurrencyValue(Double currencyValue) {
        this.currencyValue = currencyValue;
    }

    public String getBaseCurrencyAbbreviation() {
        return baseCurrencyAbbreviation;
    }

    public void setBaseCurrencyAbbreviation(String baseCurrencyAbbreviation) {
        this.baseCurrencyAbbreviation = baseCurrencyAbbreviation;
    }

    public String getTargetCurrencyAbbreviation() {
        return targetCurrencyAbbreviation;
    }

    public void setTargetCurrencyAbbreviation(String targetCurrencyAbbreviation) {
        this.targetCurrencyAbbreviation = targetCurrencyAbbreviation;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
