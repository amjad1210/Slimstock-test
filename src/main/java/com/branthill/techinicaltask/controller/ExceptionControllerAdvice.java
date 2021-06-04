package com.branthill.techinicaltask.controller;

import com.branthill.techinicaltask.exception.RestApiException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(RestApiException.class)
    public ResponseEntity<Object> handleApiErrorException(RestApiException exception) {
        return ResponseEntity.status(exception.getStatus()).body(exception);
    }

}

