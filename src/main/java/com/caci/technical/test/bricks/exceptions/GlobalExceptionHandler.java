package com.caci.technical.test.bricks.exceptions;

import com.caci.technical.test.bricks.model.Error;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FailedOrderException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Error handleFailedOrderException(FailedOrderException ex){
        return new Error(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
