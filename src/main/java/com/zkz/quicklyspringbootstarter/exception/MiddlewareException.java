package com.zkz.quicklyspringbootstarter.exception;

public class MiddlewareException extends BaseException {
    public MiddlewareException(String message) {
        super(message);
    }

    public MiddlewareException(int code, String message) {
        super(code, message);
    }
}
