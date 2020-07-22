package com.rybka.controller.rest;

import com.rybka.constant.EndpointConstants;
import com.rybka.model.dto.BankResponse;
import com.rybka.service.exchange.BankAggregatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(EndpointConstants.BANK_ROOT_URL)
@RequiredArgsConstructor
public class BankController {
    private final BankAggregatorService service;

    @GetMapping(path = EndpointConstants.BANK_GET_URL)
    public BankResponse retrieveBankData(@PathVariable String base) {
        return service.loadExchangeRatesWithSummary(base);
    }
}
