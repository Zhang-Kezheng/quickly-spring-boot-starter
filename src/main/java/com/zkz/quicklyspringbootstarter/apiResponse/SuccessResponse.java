package com.zkz.quicklyspringbootstarter.apiResponse;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SuccessResponse extends ApiResponse {
    private Object data;

    public SuccessResponse() {
        super(ApiResponse.STATUS_SUCCESS);
    }

    public SuccessResponse(Object data) {
        super(ApiResponse.STATUS_SUCCESS);
        this.data = data;
    }
}
