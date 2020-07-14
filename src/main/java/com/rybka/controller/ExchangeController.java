package com.rybka.controller;

import com.rybka.model.CurrencyData;
import com.rybka.model.ExchangeRequest;
import com.rybka.model.ExchangeResultData;
import com.rybka.service.exchange.ExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("exchange")
@RequiredArgsConstructor
public class ExchangeController {
    private final ExchangeService service;

    @GetMapping(path = "/{base}/{target}")
    public CurrencyData retrieveExchangeRates(@PathVariable String base, @PathVariable String target) {
        return service.loadCurrencyOf(base.toUpperCase(), target.toUpperCase());
    }

    @PostMapping
    public ExchangeResultData retrieveExchangeRatesWithAmount(@RequestBody ExchangeRequest request) {
        return service.retrieveTopCurrency(request.getBase(), request.getType(), request.getCount());
    }
}
