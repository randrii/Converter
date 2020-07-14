package com.rybka.controller;

import com.rybka.model.BankData;
import com.rybka.service.exchange.BankAggregatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("banks")
@RequiredArgsConstructor
public class BankController {
    private final BankAggregatorService service;

    @GetMapping(path = "/{base}")
    public List<BankData> retrieveBankData(@PathVariable String base) {
        return service.loadExchangeRatesPageFor(base);
    }
}
