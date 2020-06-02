package com.rybka.service;

import com.rybka.model.ExchangeResponse;

public interface BaseApiConnectorService {
    ExchangeResponse retrieveRates(String userBaseCurrency);
}
