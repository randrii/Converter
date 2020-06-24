package com.rybka.service.exchange;

import com.rybka.constant.ExchangeType;
import com.rybka.constant.PropertyInfo;
import com.rybka.exception.MissedBaseCurrencyException;
import com.rybka.model.CurrencyData;
import com.rybka.model.ExchangeResponse;
import com.rybka.model.ExchangeResultData;
import com.rybka.model.TopCurrencyData;
import com.rybka.service.connector.BaseCurrencyExchangeConnector;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Service
@Data
@AllArgsConstructor
public class ExchangeService {
    private BaseCurrencyExchangeConnector connectorService;
    private final Environment environment;
    private Map<String, BiFunction<String, Double, List<TopCurrencyData>>> exchangeTypeMap;

    public CurrencyData loadCurrencyOf(String userBaseCurrency, String userTargetCurrency) {
        var exchangeResponse = connectorService.retrieveRates(userBaseCurrency);
        var targetCurrency = retrieveTargetCurrency(exchangeResponse, userTargetCurrency).orElseThrow(() -> new MissedBaseCurrencyException("Target currency not found!"));
        return new CurrencyData(userBaseCurrency, targetCurrency.getKey(), targetCurrency.getValue());
    }

    public Double calculateTotal(Double rate, Double amount) {
        return rate * amount;
    }

    public ExchangeResultData retrieveTopCurrency(String base, String exchangeType, double count) {
        var topCurrencyList = exchangeTypeMap.getOrDefault(exchangeType, this::sellBaseCurrency).apply(base, count);

        return ExchangeResultData.builder()
                .base(base)
                .count(count)
                .topCurrencyDataList(topCurrencyList)
                .build();
    }

    @PostConstruct
    private void initExchangeTypeMap() {
        exchangeTypeMap.put(ExchangeType.BUY.name(), this::buyBaseCurrency);
        exchangeTypeMap.put(ExchangeType.SELL.name(), this::sellBaseCurrency);
    }

    private List<TopCurrencyData> buyBaseCurrency(String base, double count) {
        var popularCurrencyList = Objects.requireNonNull(environment.getProperty(PropertyInfo.PROPERTY_EXCHANGE_CURRENCY)).split(",");

        return Arrays.stream(popularCurrencyList)
                .map(s -> obtainBuyBaseForTopCurrency(s, base, count))
                .collect(Collectors.toList());
    }

    private List<TopCurrencyData> sellBaseCurrency(String base, double count) {
        var popularCurrencyList = Objects.requireNonNull(environment.getProperty(PropertyInfo.PROPERTY_EXCHANGE_CURRENCY)).split(",");

        return Arrays.stream(popularCurrencyList)
                .map(s -> obtainSellBaseForTopCurrency(s, base, count))
                .collect(Collectors.toList());
    }

    private TopCurrencyData obtainBuyBaseForTopCurrency(String topCurrency, String base, double count) {
        var response = connectorService.retrieveRates(topCurrency);
        response.setRates(Optional.ofNullable(response.getRates()).orElse(Map.of(topCurrency, 0.0)));

        var currencyRate = retrieveTargetCurrency(response, base).map(Map.Entry::getValue).orElse(0.0);

        return TopCurrencyData.builder()
                .name(topCurrency)
                .rate(currencyRate)
                .total(calculateTotal(currencyRate, count))
                .build();
    }

    private TopCurrencyData obtainSellBaseForTopCurrency(String topCurrency, String base, double count) {
        var response = connectorService.retrieveRates(base);
        var currencyRate = retrieveTargetCurrency(response, topCurrency).map(Map.Entry::getValue).orElse(0.0);

        return TopCurrencyData.builder()
                .name(topCurrency)
                .rate(currencyRate)
                .total(calculateTotal(currencyRate, count))
                .build();
    }

    private Optional<Map.Entry<String, Double>> retrieveTargetCurrency(ExchangeResponse exchangeResponse, String userTargetCurrency) {

        return exchangeResponse.getRates().entrySet().stream()
                .filter(item -> userTargetCurrency.equals(item.getKey()))
                .findFirst();
    }
}