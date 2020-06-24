package com.rybka.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class ExchangeResultData {
    private String base;
    private double count;
    List<TopCurrencyData> topCurrencyDataList;
}
