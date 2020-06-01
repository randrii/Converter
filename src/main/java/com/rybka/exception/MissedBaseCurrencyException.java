package com.rybka.exception;

public class MissedBaseCurrencyException extends RuntimeException {
    public MissedBaseCurrencyException(String message) {
        super(message);
    }
}
