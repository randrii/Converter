package com.rybka.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
public class UserConvertData {
    private String userBaseCurrency;
    private String userTargetCurrency;
    private double userValue;
}
