package com.rybka.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExchangeSource {
    EXCHANGE("exchange"),
    PRIME_EXCHANGE("prime_exchange");

    private final String source;
}
