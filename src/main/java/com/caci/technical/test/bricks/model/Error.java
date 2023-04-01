package com.caci.technical.test.bricks.model;

import org.springframework.http.HttpStatus;

public class Error {

    String message;

    HttpStatus errorCode;

    public Error(String message, HttpStatus errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(HttpStatus errorCode) {
        this.errorCode = errorCode;
    }
}
