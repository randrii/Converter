package com.rybka.exception;

public class IncorrectUserDataException extends RuntimeException {
    public IncorrectUserDataException(String message) {
        super(message);
    }
}
