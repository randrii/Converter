package com.rybka.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CurrencyAPICallException extends Exception {
    public CurrencyAPICallException(String message) {
        super(message);
    }
}
