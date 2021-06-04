package com.branthill.techinicaltask.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@JsonIgnoreProperties({"cause", "stackTrace", "localizedMessage", "suppressed"})
@Getter
public class RestApiException extends RuntimeException {

    private final HttpStatus status;

    public RestApiException(String message) {
        this(HttpStatus.BAD_REQUEST, message);
    }

    public RestApiException(HttpStatus status, String message) {
        super(message);

        this.status = status;
    }

}
