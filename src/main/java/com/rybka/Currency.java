package com.rybka;

import javax.persistence.*;

@Entity
@Table
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column
    private String currencyAbbreviation;

    @Column
    private double currencyValue;

    @Column
    private double amount;

    @Column
    private double total;

    public Currency() {
    }

    public Currency(int id, String currencyAbbreviation, double currencyValue, double amount, double total) {
        this.id = id;
        this.currencyAbbreviation = currencyAbbreviation;
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

    public String getCurrencyAbbreviation() {
        return currencyAbbreviation;
    }

    public void setCurrencyAbbreviation(String currencyAbbreviation) {
        this.currencyAbbreviation = currencyAbbreviation;
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

    @Override
    public String toString() {
        return "Currency{" +
                "id=" + id +
                ", currencyAbbreviation='" + currencyAbbreviation + '\'' +
                ", currencyValue=" + currencyValue +
                ", amount=" + amount +
                ", total=" + total +
                '}';
    }
}
