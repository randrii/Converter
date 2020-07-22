package com.rybka.controller;

import com.rybka.constant.Attribute;
import com.rybka.constant.EndpointConstants;
import com.rybka.model.BankData;
import com.rybka.model.StatisticalRate;
import com.rybka.model.dto.BankResponse;
import com.rybka.service.exchange.BankAggregatorService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BankPageController.class)
class BankPageControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BankAggregatorService bankService;
    private final Random random = new Random();

    @SneakyThrows
    @Test
    public void testOnRetrievingPage() {

        // given
        var base = "USD";
        var bankDataList = List.of(buildBankData());
        var statisticalRate = buildStatisticalRate();
        var bankResponse = new BankResponse(bankDataList, statisticalRate);

        when(bankService.loadExchangeRatesWithSummary(base)).thenReturn(bankResponse);

        // expects
        mockMvc.perform(get(EndpointConstants.BANK_ROOT_URL)
                .param("base", base))
                .andExpect(status().isOk())
                .andExpect(model().attribute(Attribute.BANK_BANKS, equalTo(bankResponse.getBankDataList())))
                .andExpect(model().attribute(Attribute.BANK_STATISTIC, equalTo(bankResponse.getStatisticalRate())));
    }

    private BankData buildBankData() {

        var base = "USD";
        var link = "/test";
        var date = LocalDateTime.now();

        return new BankData(base, link, random.nextDouble(), random.nextDouble(), date);
    }

    private StatisticalRate buildStatisticalRate() {
        var rate = random.nextDouble();

        return new StatisticalRate(rate, rate, rate, rate, rate, rate);
    }
}