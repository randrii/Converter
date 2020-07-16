package com.rybka.controller;

import com.rybka.constant.EndpointConstants;
import com.rybka.model.CurrencyData;
import com.rybka.model.dto.ExchangeRequest;
import com.rybka.model.ExchangeResultData;
import com.rybka.service.exchange.ExchangeService;
import com.rybka.util.MapperUtil;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Random;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExchangeController.class)
class ExchangeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ExchangeService service;
    private final Random random = new Random();

    @SneakyThrows
    @Test
    public void testOnProperGetResponse() {

        // given
        var currencyBase = "usd";
        var currencyTarget = "eur";
        var targetUrl = EndpointConstants.EXCHANGE_ROOT_URL + EndpointConstants.EXCHANGE_GET_URL;
        var currencyData = buildCurrencyData();

        when(service.loadCurrencyOf(currencyBase.toUpperCase(), currencyTarget.toUpperCase())).thenReturn(currencyData);

        var currencyDataJson = MapperUtil.mapToJson(currencyData);

        // expects
        mockMvc.perform(get(targetUrl, currencyBase, currencyTarget))
                .andExpect(status().isOk())
                .andExpect(content().string(currencyDataJson));
    }

    @SneakyThrows
    @Test
    public void testOnProperPostResponse() {

        // given
        var base = "USD";
        var type = "BUY";
        var count = 4d;
        var targetUrl = EndpointConstants.EXCHANGE_ROOT_URL;
        var exchangeResultData = new ExchangeResultData(base, count, List.of());
        var exchangeRequest = new ExchangeRequest(base, type, count);

        when(service.retrieveTopCurrency(base, type, count)).thenReturn(exchangeResultData);

        var exchangeResponseDataJson = MapperUtil.mapToJson(exchangeResultData);
        var exchangeRequestJson = MapperUtil.mapToJson(exchangeRequest);

        // expects
        mockMvc.perform(post(targetUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(exchangeRequestJson))
                .andExpect(status().isOk())
                .andExpect(content().string(exchangeResponseDataJson));
    }

    @SneakyThrows
    @Test
    public void testOnPostBrokenJsonParam() {

        // given
        var base = "USD";
        var type = "BUY";
        var count = 4d;
        var targetUrl = EndpointConstants.EXCHANGE_ROOT_URL;
        var exchangeResultData = new ExchangeResultData(base, count, List.of());
        var brokenExchangeRequestJson = "";

        when(service.retrieveTopCurrency(base, type, count)).thenReturn(exchangeResultData);

        // expects
        mockMvc.perform(post(targetUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(brokenExchangeRequestJson))
                .andExpect(status().isBadRequest());
    }

    private CurrencyData buildCurrencyData() {
        var currencyList = List.of("USD", "EUR", "UAH");
        var base = currencyList.get(random.nextInt(currencyList.size()));
        var target = currencyList.get(random.nextInt(currencyList.size()));
        var rate = random.nextDouble();

        return new CurrencyData(base, target, rate);
    }
}