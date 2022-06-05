package com.zkz.quicklyspringbootstarter.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BaseException extends RuntimeException {
    /**
     * 异常编号
     */
    private int code;
    /**
     * 错误信息
     */
    private String message;


    public BaseException(String message) {
        this.code = 500;
        this.message = message;
    }

    public BaseException(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
