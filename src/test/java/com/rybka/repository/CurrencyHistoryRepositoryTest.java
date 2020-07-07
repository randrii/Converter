package com.rybka.repository;

import com.rybka.model.CurrencyHistory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class CurrencyHistoryRepositoryTest {
    @Autowired
    private CurrencyHistoryRepository repository;

    @Test
    public void testOnCorrectHistorySave() {

        // given
        var currencyBase = "USD";
        var currencyTarget = "EUR";
        var currencyRate = 0.88;
        var currencyAmount = 5d;
        var currencyTotal = currencyRate * currencyAmount;
        var testData = new CurrencyHistory(currencyBase, currencyTarget, currencyAmount, currencyRate, currencyTotal);

        // when
        var savedData = repository.save(testData);

        // then
        assertEquals(testData.getBase(), savedData.getBase());
    }

    @Test
    public void testOnCorrectHistoryFindAll() {

        // given
        var currencyBase = "USD";
        var currencyTarget = "EUR";
        var currencyRate = 0.88;
        var currencyAmount = 5d;
        var currencyTotal = currencyRate * currencyAmount;
        var testData = new CurrencyHistory(currencyBase, currencyTarget, currencyAmount, currencyRate, currencyTotal);
        var emptyTestHistory = new CurrencyHistory();
        var expectedResultListSize = 2;

        repository.save(testData);
        repository.save(emptyTestHistory);

        // when
        var actualResultList = repository.findAll();

        // then
        assertEquals(expectedResultListSize, actualResultList.size());
    }

    @Test
    public void testOnCorrectHistoryFindTop5() {

        // given
        var currencyBase = "USD";
        var currencyTarget = "EUR";
        var currencyRate = 0.88;
        var currencyAmount = 5d;
        var currencyTotal = currencyRate * currencyAmount;
        var testData = new CurrencyHistory(currencyBase, currencyTarget, currencyAmount, currencyRate, currencyTotal);
        var expectedResultListSize = 5;
        var testHistoryList = List.of(
                testData, new CurrencyHistory(), new CurrencyHistory(), new CurrencyHistory(), new CurrencyHistory(), new CurrencyHistory()
        );

        repository.saveAll(testHistoryList);

        // when
        var actualResultList = repository.findTop5ByOrderByIdDesc();

        // then
        assertFalse(actualResultList.contains(testData));
        assertEquals(expectedResultListSize, actualResultList.size());
    }

    @Test
    void testOnZeroHistoryFindAll() {

        // given
        var expectedZeroResultListSize = 0;

        // when
        var actualResultList = repository.findAll();

        // then
        assertEquals(expectedZeroResultListSize, actualResultList.size());
    }

    @Test
    void testOnNotEnoughHistoryTop5() {

        // given
        var currencyBase = "USD";
        var currencyTarget = "EUR";
        var currencyRate = 0.88;
        var currencyAmount = 5d;
        var currencyTotal = currencyRate * currencyAmount;
        var testData = new CurrencyHistory(currencyBase, currencyTarget, currencyAmount, currencyRate, currencyTotal);
        var expectedResultListSize = 4;
        var testHistoryList = List.of(
                new CurrencyHistory(), new CurrencyHistory(), new CurrencyHistory(), testData
        );

        repository.saveAll(testHistoryList);

        // when
        var actualResultList = repository.findTop5ByOrderByIdDesc();

        // then
        assertEquals(expectedResultListSize, actualResultList.size());
    }
}