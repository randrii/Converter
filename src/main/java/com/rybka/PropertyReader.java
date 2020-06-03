package com.rybka;

import com.rybka.exception.InvalidPropertyException;
import com.rybka.service.BaseCurrencyExchangeConnector;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public class PropertyReader {
    private final Map<String, BaseCurrencyExchangeConnector> connectorMap;

    public PropertyReader(Map<String, BaseCurrencyExchangeConnector> connectorMap) {
        this.connectorMap = connectorMap;
    }

    public String getPropertyValue() {
        Properties properties = new Properties();
        try (FileInputStream in = new FileInputStream("application.properties")) {
            properties.load(in);
            in.close();

            return properties.getProperty("exchange.source");
        } catch (IOException e) {
            e.printStackTrace();
            throw new InvalidPropertyException("Unable to get specified property. Reason: " + e.getMessage());
        }
    }

    public BaseCurrencyExchangeConnector getObjectOf(String source) {
        return connectorMap.entrySet().stream()
                .filter(item -> source.equals(item.getKey()))
                .findFirst()
                .orElseThrow(() -> new InvalidPropertyException("Cannot find specified source."))
                .getValue();
    }
}
