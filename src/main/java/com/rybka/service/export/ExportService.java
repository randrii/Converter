package com.rybka.service.export;

import com.rybka.model.CurrencyHistory;

import java.util.List;

public interface ExportService {
    void export(List<CurrencyHistory> histories);
}
