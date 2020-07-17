package com.rybka.controller;

import com.rybka.constant.EndpointConstants;
import com.rybka.model.CurrencyHistory;
import com.rybka.model.dto.ExchangeRequest;
import com.rybka.repository.CurrencyHistoryRepository;
import com.rybka.service.exchange.ExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = {EndpointConstants.ROOT_URL, EndpointConstants.EXCHANGE_ROOT_URL})
@RequiredArgsConstructor
public class ExchangePageController {
    private final ExchangeService service;
    private final CurrencyHistoryRepository repository;

    @GetMapping
    public String showExchangePage(Model model) {
        model.addAttribute("exchangeRequest", new ExchangeRequest());

        return "exchangePage";
    }

    @PostMapping(EndpointConstants.EXCHANGE_POST_URL)
    public String calculateExchange(@ModelAttribute ExchangeRequest exchangeRequest, Model model) {
        var data = service.retrieveTopCurrency(exchangeRequest.getBase(), exchangeRequest.getType(), exchangeRequest.getCount());
        var topCurrencyList = data.getTopCurrencyDataList();
        var history = convertToHistory(exchangeRequest);

        repository.save(history);

        var historyList = repository.findTop5ByOrderByDateDesc();

        model.addAttribute("topCurrency", topCurrencyList);
        model.addAttribute("histories", historyList);

        return "exchangePage";
    }

    private CurrencyHistory convertToHistory(ExchangeRequest exchangeRequest) {
        var history = new CurrencyHistory();
        history.setBase(exchangeRequest.getBase());
        history.setAmount(exchangeRequest.getCount());
        history.setTarget(exchangeRequest.getType());

        return history;
    }
}
