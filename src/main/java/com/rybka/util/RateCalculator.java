package com.rybka.util;

import com.rybka.model.BankData;
import com.rybka.model.StatisticalRate;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.function.ToDoubleFunction;

public class RateCalculator {

    public static StatisticalRate calculateStatisticalRate(List<BankData> bankData) {
        var summaryBuy = retrieveSummaryFor(bankData, BankData::getBuy);
        var summarySell = retrieveSummaryFor(bankData, BankData::getSell);

        return new StatisticalRate(summaryBuy.getMin(), summarySell.getMin(), summaryBuy.getMax(), summarySell.getMax(), summaryBuy.getAverage(), summarySell.getAverage());
    }

    private static DoubleSummaryStatistics retrieveSummaryFor(List<BankData> bankData, ToDoubleFunction<BankData> mapFunction) {
        return bankData.stream()
                .mapToDouble(mapFunction)
                .summaryStatistics();
    }
}
