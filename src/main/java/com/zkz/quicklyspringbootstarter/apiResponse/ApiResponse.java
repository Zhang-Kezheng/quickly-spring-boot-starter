package com.zkz.quicklyspringbootstarter.apiResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {
    public static final int STATUS_ERROR = 0;
    public static final int STATUS_SUCCESS = 1;

    private Integer success;
}
