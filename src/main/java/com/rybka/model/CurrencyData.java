package com.rybka.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
public class CurrencyData {
    private String base;
    private String target;
    private double rate;
}
