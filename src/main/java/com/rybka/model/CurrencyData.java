package com.rybka.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class CurrencyData {
    private String base;
    private String target;
    private double rate;
}
