package com.rybka.util;

import com.rybka.model.BankData;
import com.rybka.model.StatisticalRate;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RateCalculatorTest {

    private final Random random = new Random();

    @Test
    public void testOnProperCalculation() {

        // given
        var bankDataList = List.of(buildBankData(), buildBankData(), buildBankData());
        var expectedSummary = constructCalculatedRates(bankDataList);

        // when
        var actualSummary = RateCalculator.calculateStatisticalRate(bankDataList);

        // then
        assertEquals(expectedSummary, actualSummary);
    }

    @Test
    public void testOnInvalidValue() {

        // given
        List<BankData> bankDataList = List.of();
        var expectedValue = 0;
        var expectedSummary = new StatisticalRate(expectedValue, expectedValue, expectedValue, expectedValue, expectedValue, expectedValue);

        // when
        var actualSummary = RateCalculator.calculateStatisticalRate(bankDataList);

        // then
        assertEquals(expectedSummary, actualSummary);
    }

    private StatisticalRate constructCalculatedRates(List<BankData> bankData) {
        var minBuyRate = bankData.stream()
                .mapToDouble(BankData::getBuy)
                .min()
                .orElse(0);
        var minSellRate = bankData.stream()
                .mapToDouble(BankData::getSell)
                .min()
                .orElse(0);
        var maxBuyRate = bankData.stream()
                .mapToDouble(BankData::getBuy)
                .max()
                .orElse(0);
        var maxSellRate = bankData.stream()
                .mapToDouble(BankData::getSell)
                .max()
                .orElse(0);
        var avgBuyRate = bankData.stream()
                .mapToDouble(BankData::getBuy)
                .average()
                .orElse(0);
        var avgSellRate = bankData.stream()
                .mapToDouble(BankData::getSell)
                .average()
                .orElse(0);

        return new StatisticalRate(minBuyRate, minSellRate, maxBuyRate, maxSellRate, avgBuyRate, avgSellRate);
    }

    private BankData buildBankData() {
        var name = "testBank";
        var link = "/testLink";
        var date = LocalDateTime.now();

        return new BankData(name, link, random.nextDouble(), random.nextDouble(), date);
    }
}