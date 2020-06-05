package com.rybka.service.connector;

import com.rybka.model.ExchangeResponse;

public interface BaseCurrencyExchangeConnector {
    ExchangeResponse retrieveRates(String userBaseCurrency);
}
