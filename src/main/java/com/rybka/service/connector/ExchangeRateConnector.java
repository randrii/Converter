package com.rybka.service.connector;

import com.rybka.constant.ExchangeSource;
import com.rybka.constant.Messages;
import com.rybka.exception.CurrencyAPICallException;
import com.rybka.model.dto.ExchangeResponse;
import com.rybka.properties.ExchangeConnectorProperty;
import coresearch.cvurl.io.request.CVurl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component(ExchangeSource.EXCHANGE_SOURCE)
@Slf4j
@RequiredArgsConstructor
public class ExchangeRateConnector implements BaseCurrencyExchangeConnector {
    private final CVurl cVurl;
    private final ExchangeConnectorProperty connectorProperty;

    public ExchangeResponse retrieveRates(String userBaseCurrency) {
        try {
            return cVurl
                    .get(String.format(connectorProperty.getUrl(), userBaseCurrency))
                    .asObject(ExchangeResponse.class);
        } catch (Exception exception) {
            log.error(String.format(Messages.API_CALL_EXCEPTION_MSG, ExchangeSource.EXCHANGE_SOURCE) + exception.getMessage());
            throw new CurrencyAPICallException(String.format(Messages.API_CALL_EXCEPTION_MSG, ExchangeSource.EXCHANGE_SOURCE) + exception.getMessage());
        }
    }
}
