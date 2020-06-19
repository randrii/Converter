package com.rybka.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@ToString
public class ExchangeResultData {
    private String base;
    private double count;
    List<TopCurrencyData> topCurrencyDataList;
}
