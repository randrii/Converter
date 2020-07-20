package com.rybka.repository;

import com.rybka.model.ExchangeHistory;
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
class ExchangeHistoryRepositoryTest {
    @Autowired
    private ExchangeHistoryRepository repository;
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
        var latestHistoryList = List.of(
                buildRandomHistory(), buildRandomHistory(), buildRandomHistory(), buildRandomHistory(), buildRandomHistory()
        );
        var oldHistoryList = List.of(
                buildRandomHistory(), buildRandomHistory(), buildRandomHistory(), buildRandomHistory(), buildRandomHistory()
        );

        repository.saveAll(oldHistoryList);
        repository.saveAll(latestHistoryList);

        // when
        var actualResultList = repository.findTop5ByOrderByDateDesc();

        // then
        assertEquals(latestHistoryList.stream().sorted(Comparator.comparing(ExchangeHistory::getId)).collect(Collectors.toList()),
                actualResultList.stream().sorted(Comparator.comparing(ExchangeHistory::getId)).collect(Collectors.toList())
        );
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

    private ExchangeHistory buildRandomHistory() {
        var currencyList = List.of("USD", "EUR", "PLN", "UAH");
        var exchangeTypeList = List.of("SELL", "BUY");
        var currencyBase = currencyList.get(random.nextInt(currencyList.size()));
        var currencyType = exchangeTypeList.get(random.nextInt(exchangeTypeList.size()));
        var currencyCount = random.nextDouble();

        return new ExchangeHistory(currencyBase, currencyType, currencyCount);
    }
}