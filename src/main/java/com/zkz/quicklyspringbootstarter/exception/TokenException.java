package com.zkz.quicklyspringbootstarter.exception;

public class TokenException extends RuntimeException {
    private String message;
    public TokenException(String message) {
        super(message);
        this.message = message;
    }
}
