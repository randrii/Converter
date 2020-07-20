package com.rybka.controller;

import com.rybka.constant.Attribute;
import com.rybka.constant.EndpointConstants;
import com.rybka.model.ExchangeHistory;
import com.rybka.model.ExchangeResultData;
import com.rybka.model.TopCurrencyData;
import com.rybka.model.dto.ExchangeRequest;
import com.rybka.repository.ExchangeHistoryRepository;
import com.rybka.service.exchange.ExchangeService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExchangePageController.class)
class ExchangePageControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ExchangeService service;
    @MockBean
    private ExchangeHistoryRepository repository;

    @SneakyThrows
    @Test
    public void testOnShowingPage() {

        // given
        var exchangeRequest = new ExchangeRequest();

        // expects
        mockMvc.perform(get(EndpointConstants.EXCHANGE_ROOT_URL))
                .andExpect(status().isOk())
                .andExpect(model().attribute(Attribute.EXCHANGE_REQUEST, equalTo(exchangeRequest)));
    }

    @SneakyThrows
    @Test
    public void testOnCalculatingExchange() {

        // given
        var base = "USD";
        var type = "SELL";
        var count = 2d;
        var rate = 26d;
        var total = 104d;
        var topCurrency = new TopCurrencyData(base, rate, total);
        var topCurrencyList = List.of(topCurrency);
        var resultData = new ExchangeResultData(base, count, topCurrencyList);
        var exchangeHistory = new ExchangeHistory(base, type, count);
        var exchangeRequest = new ExchangeRequest(base, type, count);
        var exchangeHistoryList = List.of(exchangeHistory);

        when(service.retrieveTopCurrency(anyString(), anyString(), anyDouble())).thenReturn(resultData);
        when(repository.save(exchangeHistory)).thenReturn(exchangeHistory);
        when(repository.findTop5ByOrderByDateDesc()).thenReturn(exchangeHistoryList);

        // expects
        mockMvc.perform(post(EndpointConstants.EXCHANGE_ROOT_URL + EndpointConstants.EXCHANGE_POST_URL)
                .param("base", exchangeRequest.getBase())
                .param("type", exchangeRequest.getType())
                .param("count", String.valueOf(exchangeRequest.getCount()))
                .flashAttr(Attribute.EXCHANGE_REQUEST, exchangeRequest))
                .andExpect(status().isOk())
                .andExpect(model().attribute(Attribute.EXCHANGE_TOP_CURRENCY, equalTo(topCurrencyList)))
                .andExpect(model().attribute(Attribute.EXCHANGE_HISTORY, equalTo(exchangeHistoryList)));

    }

    @SneakyThrows
    @Test
    public void testOnBrokenRequest() {

        // given
        var base = "USD";
        var type = "SELL";
        var count = 2d;
        var rate = 26d;
        var total = 104d;
        var topCurrency = new TopCurrencyData(base, rate, total);
        var topCurrencyList = List.of(topCurrency);
        var resultData = new ExchangeResultData(base, count, topCurrencyList);
        var exchangeHistory = new ExchangeHistory(base, type, count);
        var brokenExchangeRequest = new ExchangeRequest(null, null, 0d);
        var exchangeHistoryList = List.of(exchangeHistory);

        when(service.retrieveTopCurrency(anyString(), anyString(), anyDouble())).thenReturn(resultData);
        when(repository.save(exchangeHistory)).thenReturn(exchangeHistory);
        when(repository.findTop5ByOrderByDateDesc()).thenReturn(exchangeHistoryList);

        // expects
        mockMvc.perform(post(EndpointConstants.EXCHANGE_ROOT_URL + EndpointConstants.EXCHANGE_POST_URL)
                .param("base", brokenExchangeRequest.getBase())
                .param("type", brokenExchangeRequest.getType())
                .param("count", String.valueOf(brokenExchangeRequest.getCount()))
                .flashAttr(Attribute.EXCHANGE_REQUEST, brokenExchangeRequest))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }
}