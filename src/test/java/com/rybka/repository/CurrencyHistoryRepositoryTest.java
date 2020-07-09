package com.rybka.repository;

import com.rybka.model.CurrencyHistory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class CurrencyHistoryRepositoryTest {
    @Autowired
    private CurrencyHistoryRepository repository;
    private final Random random = new Random();

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
        var historyTop5List = List.of(
                buildRandomHistory(), buildRandomHistory(), buildRandomHistory(), buildRandomHistory(), buildRandomHistory()
        );
        var historyOtherList = List.of(
                buildRandomHistory(), buildRandomHistory(), buildRandomHistory()
        );
        repository.saveAll(historyOtherList);
        repository.saveAll(historyTop5List);

        // when
        var actualResultList = repository.findTop5ByOrderByIdDesc();

        // then
        var expectedHistoryList = historyTop5List.stream()
                .sorted(Comparator.comparingInt(CurrencyHistory::getId).reversed())
                .collect(Collectors.toList());

        assertEquals(expectedHistoryList, actualResultList);
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
        var currencyBase = currencyList.get(random.nextInt(currencyList.size()));
        var currencyTarget = currencyList.get(random.nextInt(currencyList.size()));
        var currencyRate = random.nextDouble();
        var currencyAmount = random.nextInt();
        var total = currencyRate * currencyAmount;

        return new CurrencyHistory(currencyBase, currencyTarget, currencyAmount, currencyRate, total);
    }
}