package com.rybka.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rybka.CurrencyAPIConstants;
import com.rybka.exception.CurrencyAPICallException;
import com.rybka.model.ExchangeResponse;
import coresearch.cvurl.io.request.CVurl;
import lombok.extern.log4j.Log4j;

@Log4j
public class ExchangeRatesApiConnectorService implements BaseApiConnectorService {

    private final ObjectMapper mapper;

    public ExchangeRatesApiConnectorService(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public ExchangeResponse retrieveRates(String userBaseCurrency) {

        try {
            CVurl cVurl = new CVurl();
            var response = cVurl.get(String.format(CurrencyAPIConstants.EXCHANGE_RATE__API_URL, userBaseCurrency))
                    .asString()
                    .orElseThrow(RuntimeException::new).getBody();
            return mapper.readValue(response, ExchangeResponse.class);
        } catch (JsonProcessingException exception) {
            log.error("Error while calling Currency API service 2. Reason: " + exception.getMessage());
            throw new CurrencyAPICallException("Wrong parameters in Currency API 2 request! Reason: " +
                    exception.getMessage());
        }

    }
}
