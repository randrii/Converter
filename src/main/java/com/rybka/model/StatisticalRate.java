package com.rybka.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatisticalRate {
    private double minBuyRate;
    private double minSellRate;
    private double maxBuyRate;
    private double maxSellRate;
    private double avgBuyRate;
    private double avgSellRate;
}
