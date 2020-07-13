package com.rybka.controller;

import com.rybka.model.CurrencyHistory;
import com.rybka.repository.CurrencyHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("history")
public class HistoryController {
    @Autowired
    private CurrencyHistoryRepository repository;

    @GetMapping(produces = "application/json")
    public List<CurrencyHistory> retrieveExchangeHistory() {
        return repository.findTop5ByOrderByDateDesc();
    }
}
