package com.rybka.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
public class ExchangeHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String base;
    private String type;
    private double count;
    @CreationTimestamp
    private Date date;

    public ExchangeHistory(String base, String type, double count) {
        this.base = base;
        this.type = type;
        this.count = count;
    }
}
