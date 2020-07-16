package com.rybka.service.connector;

import com.rybka.model.dto.ExchangeResponse;

public interface BaseCurrencyExchangeConnector {
    ExchangeResponse retrieveRates(String userBaseCurrency);
}
