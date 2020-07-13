package com.rybka.controller;

import com.rybka.model.BankData;
import com.rybka.service.exchange.BankAggregatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("banks")
public class BankController {
    @Autowired
    private BankAggregatorService service;

    @GetMapping(path = "/{base}", produces = "application/json")
    public List<BankData> retrieveBankData(@PathVariable String base) {
        return service.loadExchangeRatesPageFor(base);
    }
}
