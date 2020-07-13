package com.rybka.controller;

import com.rybka.model.CurrencyData;
import com.rybka.model.ExchangeResultData;
import com.rybka.service.exchange.ExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("exchange")
public class ExchangeController {
    @Autowired
    private ExchangeService service;

    @GetMapping(path = "/{base}/{target}", produces = "application/json")
    public CurrencyData retrieveExchangeRates(@PathVariable String base, @PathVariable String target) {
        return service.loadCurrencyOf(base.toUpperCase(), target.toUpperCase());
    }

    @PostMapping(produces = "application/json")
    public ExchangeResultData retrieveExchangeRatesWithAmount(@ModelAttribute("base") String base, @ModelAttribute("type") String type, @ModelAttribute("count") double count) {
        return service.retrieveTopCurrency(base.toUpperCase(), type.toUpperCase(), count);
    }
}
