package com.rybka.service;

import com.rybka.model.CurrencyHistory;

import java.util.List;

public interface ExportService {
    void export(List<CurrencyHistory> histories);
}
