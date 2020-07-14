package com.rybka.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rybka.model.CurrencyData;
import com.rybka.model.ExchangeRequest;
import com.rybka.model.ExchangeResultData;
import com.rybka.service.exchange.ExchangeService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Random;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@WebMvcTest(ExchangeController.class)
class ExchangeControllerTest {
    private MockMvc mockMvc;
    @MockBean
    private ExchangeService service;
    private final Random random = new Random();
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        var controller = new ExchangeController(service);
        mockMvc = standaloneSetup(controller).build();
    }

    @SneakyThrows
    @Test
    public void testOnProperGetResponse() {
        var currencyBase = "usd";
        var currencyTarget = "eur";
        var targetUrl = "/exchange/" + currencyBase + "/" + currencyTarget;
        var currencyData = buildCurrencyData();

        when(service.loadCurrencyOf(anyString().toUpperCase(), anyString().toUpperCase())).thenReturn(currencyData);

        var currencyDataJson = mapToJson(currencyData);

        mockMvc.perform(get(targetUrl))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(currencyDataJson));
    }

    @SneakyThrows
    @Test
    public void testOnProperPostResponse() {
        var base = "USD";
        var type = "BUY";
        var count = 4d;
        var targetUrl = "/exchange";
        var exchangeResultData = new ExchangeResultData(base, count, List.of());
        var exchangeRequest = new ExchangeRequest(base, type, count);
        when(service.retrieveTopCurrency(anyString(), anyString(), anyDouble())).thenReturn(exchangeResultData);

        var exchangeResponseDataJson = mapToJson(exchangeResultData);
        var exchangeRequestJson = mapToJson(exchangeRequest);

        mockMvc.perform(post(targetUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(exchangeRequestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(exchangeResponseDataJson));
    }

    private CurrencyData buildCurrencyData() {
        var currencyList = List.of("USD", "EUR", "UAH");
        var base = currencyList.get(random.nextInt(currencyList.size()));
        var target = currencyList.get(random.nextInt(currencyList.size()));
        var rate = random.nextDouble();

        return new CurrencyData(base, target, rate);
    }

    @SneakyThrows
    private <T> String mapToJson(T data) {
        return mapper.writeValueAsString(data);
    }
}