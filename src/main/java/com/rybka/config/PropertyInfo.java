package com.rybka.config;

import java.io.File;

public final class PropertyInfo {
    public static final String RESOURCE_PATH = "src" + File.separator + "main" + File.separator + "resources" + File.separator;
    public static final String PROPERTY_FILE_NAME = RESOURCE_PATH + "application.properties";
    public static final String PROPERTY_EXCHANGE_SOURCE = "exchange.source";
    public static final String PROPERTY_EXCHANGE_KEY = "exchange";
    public static final String PROPERTY_PRIME_EXCHANGE_KEY = "prime_exchange";
}
