package com.rybka.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrencyResponse {
    private String base;
    private String target;
    private double rate;
}
