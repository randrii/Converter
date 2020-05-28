package com.rybka.exception;

public class ZeroCurrencyRateException extends Exception {
    public ZeroCurrencyRateException(String message) {
        super(message);
    }
}
