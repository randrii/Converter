package com.rybka.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
public class Currency {
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
}
