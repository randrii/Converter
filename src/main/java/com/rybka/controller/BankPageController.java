package com.rybka.controller;

import com.rybka.constant.EndpointConstants;
import com.rybka.constant.Attribute;
import com.rybka.constant.Pages;
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
    private final BankAggregatorService bankService;

    @GetMapping
    public String showBankPage(@RequestParam(value = "base", defaultValue = "usd") String base, Model model) {
        var bankExchangeRates = bankService.loadExchangeRatesWithSummary(base);
        var banks = bankExchangeRates.getBankDataList();
        var summary = bankExchangeRates.getStatisticalRate();

        model.addAttribute(Attribute.BANK_BANKS, banks);
        model.addAttribute(Attribute.BANK_STATISTIC, summary);

        return Pages.BANK_PAGE;
    }
}
