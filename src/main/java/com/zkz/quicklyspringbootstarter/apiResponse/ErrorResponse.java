package com.zkz.quicklyspringbootstarter.apiResponse;

import com.zkz.quicklyspringbootstarter.exception.BaseException;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ErrorResponse extends ApiResponse {
    private int code;
    private String message;


    public ErrorResponse(int code) {
        this(code,null);
    }

    public ErrorResponse(int code, String extraMsg) {
        super(ApiResponse.STATUS_ERROR);
        this.code=code;
        this.message=extraMsg;
    }

    public ErrorResponse(BaseException baseException) {
        this(500, baseException.getMessage());
    }
}
