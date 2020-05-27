package com.rybka.model;

import javax.persistence.*;

@Entity
@Table
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column
    private String baseCurrencyAbbreviation;

    @Column
    private String targetCurrencyAbbreviation;

    @Column
    private double currencyValue;

    @Column
    private double amount;

    @Column
    private double total;

    public Currency() {
    }

    public Currency(String baseCurrencyAbbreviation, String targetCurrencyAbbreviation, double currencyValue, double amount, double total) {
        this.baseCurrencyAbbreviation = baseCurrencyAbbreviation;
        this.targetCurrencyAbbreviation = targetCurrencyAbbreviation;
        this.currencyValue = currencyValue;
        this.amount = amount;
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBaseCurrencyAbbreviation() {
        return baseCurrencyAbbreviation;
    }

    public void setBaseCurrencyAbbreviation(String currencyAbbreviation) {
        this.baseCurrencyAbbreviation = currencyAbbreviation;
    }

    public double getCurrencyValue() {
        return currencyValue;
    }

    public void setCurrencyValue(double currencyValue) {
        this.currencyValue = currencyValue;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getTargetCurrencyAbbreviation() {
        return targetCurrencyAbbreviation;
    }

    public void setTargetCurrencyAbbreviation(String targetCurrencyAbbreviation) {
        this.targetCurrencyAbbreviation = targetCurrencyAbbreviation;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "id=" + id +
                ", baseCurrencyAbbreviation='" + baseCurrencyAbbreviation + '\'' +
                ", targetCurrencyAbbreviation='" + targetCurrencyAbbreviation + '\'' +
                ", currencyValue=" + currencyValue +
                ", amount=" + amount +
                ", total=" + total +
                '}';
    }
}
