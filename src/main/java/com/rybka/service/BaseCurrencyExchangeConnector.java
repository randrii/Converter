package com.rybka.service;

import com.rybka.model.ExchangeResponse;

public interface BaseCurrencyExchangeConnector {
    ExchangeResponse retrieveRates(String userBaseCurrency);
}
