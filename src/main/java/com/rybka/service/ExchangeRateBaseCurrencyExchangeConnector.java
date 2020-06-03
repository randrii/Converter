package com.rybka.service;

import com.rybka.CurrencyAPIConstants;
import com.rybka.model.ExchangeResponse;
import coresearch.cvurl.io.request.CVurl;
import lombok.extern.log4j.Log4j;

@Log4j
public class ExchangeRateBaseCurrencyExchangeConnector implements BaseCurrencyExchangeConnector {

    private final CVurl cVurl;

    public ExchangeRateBaseCurrencyExchangeConnector(CVurl cVurl) {
        this.cVurl = cVurl;
    }

    public ExchangeResponse retrieveRates(String userBaseCurrency) {
        return cVurl
                .get(String.format(CurrencyAPIConstants.EXCHANGE_RATE_API_URL, userBaseCurrency))
                .asObject(ExchangeResponse.class);
    }
}
