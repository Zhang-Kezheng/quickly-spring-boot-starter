package com.zkz.quicklyspringbootstarter.exception;

import com.zkz.quicklyspringbootstarter.apiResponse.ApiResponse;
import com.zkz.quicklyspringbootstarter.apiResponse.ApiResponseUtils;
import com.zkz.quicklyspringbootstarter.log.LogUtils;
import com.zkz.quicklyspringbootstarter.utils.SpringContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;

/**
 * 实现异常拦截.
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
    private static GlobalExceptionHandlerProperties globalExceptionHandlerProperties;

    @Autowired
    public void setGlobalExceptionHandlerProperties(GlobalExceptionHandlerProperties globalExceptionHandlerProperties) {
        GlobalExceptionHandler.globalExceptionHandlerProperties = globalExceptionHandlerProperties;
    }

    public GlobalExceptionHandler() {
        LogUtils.info("启用接口统一异常拦截");
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse exception(Exception ex) {
        // 根据配置决定是否打印详细错误日志
        if (globalExceptionHandlerProperties.isPrintStackTrace()) {
            ex.printStackTrace();
        }
        LogUtils.error(ex.getLocalizedMessage());

        BaseException baseException = getGeneralBaseException(ex);
        return ApiResponseUtils.error(baseException);
    }

    /**
     * 获取一般的错误信息
     *
     * @param ex 错误类
     */
    public static BaseException getGeneralBaseException(Exception ex) {

        // 获取所有AbstractExceptionFormatter的实例
        Map<String, AbstractExceptionFormatter> formatterMap = SpringContextUtils.getBeansOfType(AbstractExceptionFormatter.class);
        // 变成集合并按照从小到大的顺序排序,并处理异常,然后返回
        return formatterMap.values().stream()
                .sorted(Comparator.comparingInt(AbstractExceptionFormatter::weight))
                .map(abstractExceptionFormatter -> abstractExceptionFormatter.format(ex))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseGet(() -> new BaseException(ex.getMessage()));
    }
}
