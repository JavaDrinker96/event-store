package com.modsen.eventstore.exception;

public class NotExistEntityException extends RuntimeException{
    public NotExistEntityException(String message) {
        super(message);
    }
}