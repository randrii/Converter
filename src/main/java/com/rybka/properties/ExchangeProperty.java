package com.rybka.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "exchange")
public class ExchangeProperty {
    private String source;
    private String currency;
}
