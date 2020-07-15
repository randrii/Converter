package com.rybka.util;

import com.rybka.model.BankData;
import com.rybka.model.StatisticalRate;

import java.util.List;
import java.util.function.ToDoubleFunction;

public class RateCalculator {

    public static StatisticalRate calculateStatisticalRate(List<BankData> bankData) {
        var minBuyRate = defineBankMinValue(bankData, BankData::getBuy);
        var minSellRate = defineBankMinValue(bankData, BankData::getSell);
        var maxBuyRate = defineBankMaxValue(bankData, BankData::getBuy);
        var maxSellRate = defineBankMaxValue(bankData, BankData::getSell);
        var avgBuyRate = defineBankAvgValue(bankData, BankData::getBuy);
        var avgSellRate = defineBankAvgValue(bankData, BankData::getSell);

        return new StatisticalRate(minBuyRate, minSellRate, maxBuyRate, maxSellRate, avgBuyRate, avgSellRate);
    }

    private static double defineBankMinValue(List<BankData> bankData, ToDoubleFunction<BankData> mapFunction) {
        return bankData.stream()
                .mapToDouble(mapFunction)
                .min()
                .orElse(0);
    }

    private static double defineBankMaxValue(List<BankData> bankData, ToDoubleFunction<BankData> mapFunction) {
        return bankData.stream()
                .mapToDouble(mapFunction)
                .max()
                .orElse(0);
    }

    private static double defineBankAvgValue(List<BankData> bankData, ToDoubleFunction<BankData> mapFunction) {
        return bankData.stream()
                .mapToDouble(mapFunction)
                .average()
                .orElse(0);
    }
}
