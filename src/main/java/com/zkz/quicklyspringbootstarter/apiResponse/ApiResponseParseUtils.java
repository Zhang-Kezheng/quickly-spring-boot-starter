package com.zkz.quicklyspringbootstarter.apiResponse;


import com.google.gson.Gson;
import com.zkz.quicklyspringbootstarter.exception.MiddlewareException;
import org.apache.commons.lang3.StringUtils;

/**
 * Api接口返回值的解析工具类
 */
public class ApiResponseParseUtils {
    /**
     * 判断返回值是否有异常
     */
    public static boolean isSuccess(String response) {
        if (StringUtils.isBlank(response)) {
            return false;
        }

        ApiResponse apiResponse = new Gson().fromJson(response, ApiResponse.class);
        return apiResponse.getSuccess().equals(ApiResponse.STATUS_SUCCESS);
    }

    /**
     * 获取返回值中的数据
     */
    public static <T> T getData(String response, Class<T> clazz) throws MiddlewareException {
        if (isSuccess(response)) {
            SuccessResponse successResponse = new Gson().fromJson(response, SuccessResponse.class);
            return new Gson().fromJson(new Gson().toJson(successResponse.getData()), clazz);
        } else {
            return null;
        }
    }

    /**
     * 获取返回值中的错误信息
     */
    public static ErrorResponse getError(String response) {
        if (isSuccess(response)) {
            return null;
        }

        if (StringUtils.isBlank(response)) {
            return null;
        }
        return new Gson().fromJson(response, ErrorResponse.class);
    }
}
