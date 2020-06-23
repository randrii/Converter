package com.rybka.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class TopCurrencyData {
    private String name;
    private Double rate;
    private Double total;
}
