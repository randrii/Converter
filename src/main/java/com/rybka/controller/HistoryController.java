package com.rybka.controller;

import com.rybka.model.CurrencyHistory;
import com.rybka.repository.CurrencyHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("history")
@RequiredArgsConstructor
public class HistoryController {
    private final CurrencyHistoryRepository repository;

    @GetMapping
    public List<CurrencyHistory> retrieveExchangeHistory() {
        return repository.findTop5ByOrderByDateDesc();
    }
}
