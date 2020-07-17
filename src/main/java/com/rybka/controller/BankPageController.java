package com.rybka.controller;

import com.rybka.constant.EndpointConstants;
import com.rybka.service.exchange.BankAggregatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(EndpointConstants.BANK_ROOT_URL)
@RequiredArgsConstructor
public class BankPageController {
    private final BankAggregatorService service;

    @GetMapping
    public String showBankPage(@RequestParam(value = "base", defaultValue = "usd") String base, Model model) {
        var data = service.loadExchangeRatesWithSummary(base);
        var banks = data.getBankDataList();
        var summary = data.getStatisticalRate();

        model.addAttribute("banks", banks);
        model.addAttribute("stats", summary);

        return "bankRates";
    }
}
