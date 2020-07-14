package com.rybka.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExchangeRequest {
    private String base;
    private String type;
    private double count;
}
