package com.rybka.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopCurrencyData {
    private String name;
    private Double rate;
    private Double total;
}
