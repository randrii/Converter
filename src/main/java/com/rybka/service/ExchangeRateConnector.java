package com.rybka.service;

import com.rybka.config.CurrencyAPIConstants;
import com.rybka.exception.CurrencyAPICallException;
import com.rybka.model.ExchangeResponse;
import coresearch.cvurl.io.request.CVurl;
import lombok.extern.log4j.Log4j;

@Log4j
public class ExchangeRateConnector implements BaseCurrencyExchangeConnector {

    private final CVurl cVurl;

    public ExchangeRateConnector(CVurl cVurl) {
        this.cVurl = cVurl;
    }

    public ExchangeResponse retrieveRates(String userBaseCurrency) {
        try {
            return cVurl
                    .get(String.format(CurrencyAPIConstants.EXCHANGE_RATE_API_URL, userBaseCurrency))
                    .asObject(ExchangeResponse.class);
        } catch (Exception exception) {
            log.error("Error while calling Exchange Rate API service. Reason: " + exception.getMessage());
            throw new CurrencyAPICallException("An error appears while calling Exchange Rate API. Reason: " + exception.getMessage());
        }
    }
}
