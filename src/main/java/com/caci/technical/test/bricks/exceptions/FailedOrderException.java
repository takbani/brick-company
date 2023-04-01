package com.caci.technical.test.bricks.exceptions;

public class FailedOrderException extends RuntimeException {

    public FailedOrderException(String message) {
        super(message);
    }
}
