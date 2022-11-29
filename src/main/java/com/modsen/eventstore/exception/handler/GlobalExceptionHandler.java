package com.modsen.eventstore.exception.handler;

import com.modsen.eventstore.exception.BeforeTodayDateException;
import com.modsen.eventstore.exception.NotExistEntityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            NotExistEntityException.class,
            IllegalArgumentException.class,
            MethodArgumentNotValidException.class,
            ConstraintViolationException.class,
            BeforeTodayDateException.class
    })
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    protected ExceptionInfo handleException(RuntimeException e) {
        log.error("Error {} with the message {} was intercepted.", e.getClass().getSimpleName(), e.getMessage());
        return new ExceptionInfo(e.getMessage());
    }

}