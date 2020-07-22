package com.rybka.controller;

import com.rybka.constant.Attribute;
import com.rybka.constant.EndpointConstants;
import com.rybka.constant.Pages;
import com.rybka.model.ExchangeHistory;
import com.rybka.model.TopCurrencyData;
import com.rybka.model.dto.ExchangeRequest;
import com.rybka.repository.ExchangeHistoryRepository;
import com.rybka.service.exchange.ExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = {EndpointConstants.ROOT_URL, EndpointConstants.EXCHANGE_ROOT_URL})
@RequiredArgsConstructor
public class ExchangePageController {
    private final ExchangeService service;
    private final ExchangeHistoryRepository repository;

    @GetMapping
    public String showExchangePage(Model model) {
        model.addAttribute(Attribute.EXCHANGE_REQUEST, new ExchangeRequest());

        return Pages.EXCHANGE_PAGE;
    }

    @ExceptionHandler(NullPointerException.class)
    @GetMapping(EndpointConstants.ERROR_ROOT_URL)
    public String test(Model model, Exception e) {
        model.addAttribute(Attribute.ERROR_ATTRIBUTE, e);

        return Pages.ERROR_PAGE;
    }

    @PostMapping(EndpointConstants.EXCHANGE_POST_URL)
    public String calculateExchange(@ModelAttribute ExchangeRequest exchangeRequest, Model model) {
        var topCurrencyList = retrieveTopCurrencyData(exchangeRequest);

        saveExchangeHistory(exchangeRequest);

        var historyList = repository.findTop5ByOrderByDateDesc();

        model.addAttribute(Attribute.EXCHANGE_TOP_CURRENCY, topCurrencyList);
        model.addAttribute(Attribute.EXCHANGE_HISTORY, historyList);

        return Pages.EXCHANGE_PAGE;
    }

    private List<TopCurrencyData> retrieveTopCurrencyData(ExchangeRequest request) {
        var data = service.retrieveTopCurrency(request.getBase(), request.getType(), request.getCount());
        return data.getTopCurrencyDataList();
    }

    private void saveExchangeHistory(ExchangeRequest request) {
        var history = new ExchangeHistory(request.getBase(), request.getType(), request.getCount());

        repository.save(history);
    }
}
