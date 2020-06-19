package com.rybka.service.exchange;

import com.rybka.constant.PopularExchangeCurrency;
import com.rybka.exception.IncorrectUserDataException;
import com.rybka.exception.MissedBaseCurrencyException;
import com.rybka.model.CurrencyData;
import com.rybka.model.ExchangeResponse;
import com.rybka.model.ExchangeResultData;
import com.rybka.model.TopCurrencyData;
import com.rybka.service.connector.BaseCurrencyExchangeConnector;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Data
@AllArgsConstructor
public class ExchangeService {
    private BaseCurrencyExchangeConnector connectorService;

    public CurrencyData loadCurrencyOf(String userBaseCurrency, String userTargetCurrency) {
        var exchangeResponse = connectorService.retrieveRates(userBaseCurrency);
        var targetCurrency = retrieveTargetCurrency(exchangeResponse, userTargetCurrency).orElseThrow(() -> new MissedBaseCurrencyException("Target currency not found!"));
        return new CurrencyData(userBaseCurrency, targetCurrency.getKey(), targetCurrency.getValue());
    }

    public Double calculateTotal(Double rate, Double amount) {
        return rate * amount;
    }

    public ExchangeResultData retrieveTopCurrency(String base, String exchangeType, double count) {
        var exchangeResponse = connectorService.retrieveRates(base);
        var topCurrencyList = new ArrayList<TopCurrencyData>();

        if (exchangeType.equals("SELL")) {
            sellBaseCurrency(count, exchangeResponse, topCurrencyList);
        } else if (exchangeType.equals("BUY")) {
            buyBaseCurrency(base, count, topCurrencyList);
        } else throw new IncorrectUserDataException("Unknown exchange type.");

        return new ExchangeResultData(base, count, topCurrencyList);
    }

    private void buyBaseCurrency(String base, double count, List<TopCurrencyData> topCurrencyList) {
        for (PopularExchangeCurrency currency : PopularExchangeCurrency.values()) {
            var exchangeResponse1 = connectorService.retrieveRates(currency.name());

            var currencyRate = retrieveTargetCurrency(exchangeResponse1, base).map(Map.Entry::getValue).orElse(0.0);
            topCurrencyList.add(new TopCurrencyData(currency.name(), currencyRate, calculateTotal(currencyRate, count)));
        }
    }

    private void sellBaseCurrency(double count, ExchangeResponse exchangeResponse, List<TopCurrencyData> topCurrencyList) {
        for (PopularExchangeCurrency currency : PopularExchangeCurrency.values()) {
            var currencyRate = retrieveTargetCurrency(exchangeResponse, currency.name()).map(Map.Entry::getValue).orElse(0.0);
            topCurrencyList.add(new TopCurrencyData(currency.name(), currencyRate, calculateTotal(currencyRate, count)));
        }
    }

    private Optional<Map.Entry<String, Double>> retrieveTargetCurrency(ExchangeResponse exchangeResponse, String userTargetCurrency) {

        return exchangeResponse.getRates().entrySet().stream()
                .filter(item -> userTargetCurrency.equals(item.getKey()))
                .findFirst();
    }
}