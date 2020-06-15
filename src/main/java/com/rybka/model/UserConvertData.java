package com.rybka.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserConvertData {
    private String userBaseCurrency;
    private String userTargetCurrency;
    private double userValue;
}
