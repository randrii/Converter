package com.rybka.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "exchange.connector")
public class ExchangeConnectorProperty {
    private String url;
}
