package com.rybka.model;

import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class ConvertedCurrency extends ExchangeRate {

    @Column
    private double amount;

    @Column
    private double total;

    public ConvertedCurrency(String base, String target, double rate, double amount, double total) {
        super(base, target, rate);
        this.amount = amount;
        this.total = total;
    }

    @Override
    public String toString() {
        return "ConvertedCurrency{" +
                "id=" + super.getId() +
                ", base=" + super.getBase() +
                ", target=" + super.getTarget() +
                ", rate=" + super.getRate() +
                ", amount=" + amount +
                ", total=" + total +
                '}';
    }
}
