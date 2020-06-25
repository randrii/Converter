package com.rybka.service.exchange;

import com.rybka.model.CurrencyData;
import com.rybka.model.ExchangeResultData;

public interface Exchangeable {
    CurrencyData loadCurrencyOf(String userBaseCurrency, String userTargetCurrency);

    Double calculateTotal(Double rate, Double amount);

    ExchangeResultData retrieveTopCurrency(String base, String exchangeType, double count);
}
