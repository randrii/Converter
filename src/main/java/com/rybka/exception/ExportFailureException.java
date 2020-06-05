package com.rybka.exception;

public class ExportFailureException extends RuntimeException {
    public ExportFailureException(String message) {
        super(message);
    }
}
