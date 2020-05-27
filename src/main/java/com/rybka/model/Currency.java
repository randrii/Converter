package com.rybka.model;

import lombok.Data;

import javax.persistence.*;

@Data
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
}
