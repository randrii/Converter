package com.rybka.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rybka.model.BankData;
import com.rybka.service.exchange.BankAggregatorService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@WebMvcTest(BankController.class)
class BankControllerTest {

    private MockMvc mockMvc;
    @MockBean
    private BankAggregatorService service;
    private final Random random = new Random();
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        var controller = new BankController(service);
        mockMvc = standaloneSetup(controller).build();
    }

    @SneakyThrows
    @Test
    public void testOnProperGetResponse() {
        var currencyBase = "usd";
        var targetUrl = "/banks/" + currencyBase;
        var bankDataList = List.of(buildBankData(), buildBankData(), buildBankData());

        when(service.loadExchangeRatesPageFor(currencyBase)).thenReturn(bankDataList);

        var bankDataJson = mapToJson(bankDataList);

        mockMvc.perform(get(targetUrl))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(bankDataJson));
    }

    private BankData buildBankData() {
        var baseList = List.of("USD", "EUR", "UAH");
        var base = baseList.get(random.nextInt(baseList.size()));
        var link = "/test";
        var date = LocalDateTime.now();

        return new BankData(base, link, random.nextDouble(), random.nextDouble(), date);
    }

    @SneakyThrows
    private String mapToJson(List<BankData> bankData) {
        return mapper.writeValueAsString(bankData);
    }
}