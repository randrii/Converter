package com.rybka.service.connector;

import com.rybka.constant.CurrencyAPIConstants;
import com.rybka.constant.ExchangeSource;
import com.rybka.exception.CurrencyAPICallException;
import com.rybka.model.ExchangeResponse;
import coresearch.cvurl.io.request.CVurl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;

@Component(ExchangeSource.EXCHANGE_SOURCE)
@Log4j
@RequiredArgsConstructor
public class ExchangeRateConnector implements BaseCurrencyExchangeConnector {
    private final CVurl cVurl;

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
