package com.rybka.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BankData {
    private String name;
    private String link;
    private double buy;
    private double sell;
    private LocalDateTime lastUpdated;
}
