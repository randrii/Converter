package com.rybka.controller;

import com.rybka.constant.EndpointConstants;
import com.rybka.model.BankData;
import com.rybka.service.exchange.BankAggregatorService;
import com.rybka.util.MapperUtil;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BankController.class)
class BankControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BankAggregatorService service;
    private final Random random = new Random();

    @SneakyThrows
    @Test
    public void testOnProperGetResponse() {

        // given
        var currencyBase = "usd";
        var targetUrl = EndpointConstants.BANK_ROOT_URL + EndpointConstants.BANK_GET_URL;

        var bankDataList = List.of(buildBankData(), buildBankData(), buildBankData());

        when(service.loadExchangeRatesPageFor(currencyBase)).thenReturn(bankDataList);

        var bankDataJson = MapperUtil.mapToJson(bankDataList);

        // expects
        mockMvc.perform(get(targetUrl, currencyBase))
                .andExpect(status().isOk())
                .andExpect(content().string(bankDataJson));
    }

    @SneakyThrows
    @Test
    public void testOnWrongParamEmptyResponse() {

        // given
        var currencyBase = "usa";
        var targetUrl = EndpointConstants.BANK_ROOT_URL + EndpointConstants.BANK_GET_URL;
        List<BankData> emptyBankDataList = List.of();

        when(service.loadExchangeRatesPageFor(currencyBase)).thenReturn(emptyBankDataList);

        // expects
        mockMvc.perform(get(targetUrl, currencyBase))
                .andExpect(status().isOk())
                .andExpect(content().string(emptyBankDataList.toString()));
    }

    private BankData buildBankData() {

        var baseList = List.of("USD", "EUR", "UAH");
        var base = baseList.get(random.nextInt(baseList.size()));
        var link = "/test";
        var date = LocalDateTime.now();

        return new BankData(base, link, random.nextDouble(), random.nextDouble(), date);
    }
}