package com.rybka.model;

import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@MappedSuperclass
public class ExchangeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column
    private String base;

    @Column
    private String target;

    @Column
    private double rate;

    public ExchangeRate(String base, String target, double rate) {
        this.base = base;
        this.target = target;
        this.rate = rate;
    }
}
