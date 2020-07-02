package com.rybka.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "prime.exchange.connector")
public class ApplicationProperties {
    private String host;
    private String endpoint;
    private String schema;
    private String token;
}
