package com.zkz.quicklyspringbootstarter.exception;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("middleware.exception")
public class GlobalExceptionHandlerProperties {
    /**
     * 是否打印详细异常日志
     */
    private boolean printStackTrace = false;
}
