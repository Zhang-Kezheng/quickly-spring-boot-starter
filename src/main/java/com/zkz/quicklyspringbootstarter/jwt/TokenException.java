package com.zkz.quicklyspringbootstarter.jwt;

import com.zkz.quicklyspringbootstarter.exception.MiddlewareException;

class TokenException extends MiddlewareException {
    TokenException(String message) {
        super(message);
    }
}
