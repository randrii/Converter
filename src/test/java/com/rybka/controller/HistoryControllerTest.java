package com.rybka.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rybka.model.CurrencyHistory;
import com.rybka.repository.CurrencyHistoryRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Random;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@WebMvcTest(HistoryController.class)
class HistoryControllerTest {

    private MockMvc mockMvc;
    @MockBean
    private CurrencyHistoryRepository repository;
    private final Random random = new Random();
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        var controller = new HistoryController(repository);
        mockMvc = standaloneSetup(controller).build();
    }

    @SneakyThrows
    @Test
    public void testOnProperGetResponse() {
        var targetUrl = "/history";
        var historyList = List.of(buildRandomHistory(), buildRandomHistory(), buildRandomHistory(), buildRandomHistory(), buildRandomHistory());

        when(repository.findTop5ByOrderByDateDesc()).thenReturn(historyList);

        var historyDataJson = mapToJson(historyList);

        mockMvc.perform(get(targetUrl))
                .andDo(print())
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

    @SneakyThrows
    private String mapToJson(List<CurrencyHistory> histories) {
        return mapper.writeValueAsString(histories);
    }
}