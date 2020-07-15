package com.rybka.controller;

import com.rybka.constant.EndpointConstants;
import com.rybka.model.CurrencyHistory;
import com.rybka.repository.CurrencyHistoryRepository;
import com.rybka.util.MapperUtil;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Random;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HistoryController.class)
class HistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CurrencyHistoryRepository repository;
    private final Random random = new Random();

    @SneakyThrows
    @Test
    public void testOnProperGetResponse() {
        var targetUrl = EndpointConstants.HISTORY_ROOT_URL;
        var historyList = List.of(buildRandomHistory(), buildRandomHistory(), buildRandomHistory(), buildRandomHistory(), buildRandomHistory());

        when(repository.findTop5ByOrderByDateDesc()).thenReturn(historyList);

        var historyDataJson = MapperUtil.mapToJson(historyList);

        // expects
        mockMvc.perform(get(targetUrl))
                .andExpect(status().isOk())
                .andExpect(content().string(historyDataJson));
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