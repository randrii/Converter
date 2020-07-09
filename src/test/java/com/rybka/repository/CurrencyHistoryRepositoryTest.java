package com.rybka.repository;

import com.google.common.collect.Lists;
import com.rybka.model.CurrencyHistory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class CurrencyHistoryRepositoryTest {
    @Autowired
    private CurrencyHistoryRepository repository;

    @Test
    public void testOnCorrectHistorySave() {

        // given
        var history = buildRandomHistory();

        // when
        var savedData = repository.save(history);

        // then
        assertEquals(history, savedData);
    }

    @Test
    public void testOnCorrectHistoryFindAll() {

        // given
        var expectedResultListSize = repository.saveAll(List.of(buildRandomHistory(), buildRandomHistory())).size();

        // when
        var actualResultList = repository.findAll();

        // then
        assertEquals(expectedResultListSize, actualResultList.size());
    }

    @Test
    public void testOnCorrectHistoryFindTop5() {

        // given
        var historyTop5List = new java.util.ArrayList<>(List.of(
                buildRandomHistory(), buildRandomHistory(), buildRandomHistory(), buildRandomHistory(), buildRandomHistory()
        ));
        var historyOtherList = new java.util.ArrayList<>(List.of(
                buildRandomHistory(), buildRandomHistory(), buildRandomHistory()
        ));
        repository.saveAll(historyOtherList);
        repository.saveAll(historyTop5List);

        // when
        var actualResultList = repository.findTop5ByOrderByIdDesc();

        // then
        assertEquals(Lists.reverse(historyTop5List), actualResultList);
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

    private CurrencyHistory buildRandomHistory() {
        var currencyList = List.of("USD", "EUR", "PLN", "CAD", "UAH");
        var random = new Random();
        var currencyBase = currencyList.get(random.nextInt(currencyList.size()));
        var currencyTarget = currencyList.get(random.nextInt(currencyList.size()));
        var currencyRate = random.nextDouble();
        var currencyAmount = random.nextInt();
        var total = currencyRate * currencyAmount;

        return new CurrencyHistory(currencyBase, currencyTarget, currencyAmount, currencyRate, total);
    }
}