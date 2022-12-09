package com.modsen.eventstore.exception;

public class BeforeTodayDateException extends RuntimeException {
    public BeforeTodayDateException(String message) {
        super(message);
    }
}
