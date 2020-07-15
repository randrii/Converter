package com.rybka.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BankResponse {
    private List<BankData> bankDataList;
    private StatisticalRate statisticalRate;
}
