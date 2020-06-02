package com.rybka.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
public class CurrencyHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column
    private String base;

    @Column
    private String target;

    @Column
    private double amount;

    @Column
    private double rate;

    @Column
    private double total;

    @Column
    @CreationTimestamp
    private Date date;

    public CurrencyHistory(String base, String target, double amount, double rate, double total) {
        this.base = base;
        this.target = target;
        this.amount = amount;
        this.rate = rate;
        this.total = total;
    }
}
