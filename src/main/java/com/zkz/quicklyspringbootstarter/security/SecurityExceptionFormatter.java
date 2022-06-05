package com.zkz.quicklyspringbootstarter.security;

import com.zkz.quicklyspringbootstarter.exception.AbstractExceptionFormatter;
import com.zkz.quicklyspringbootstarter.exception.BaseException;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;

/**
 * 实现了{@link AbstractExceptionFormatter}
 * 安全验证相关的异常处理方法
 */
@Configuration
public class SecurityExceptionFormatter extends AbstractExceptionFormatter {
    @Override
    public int weight() {
        return Short.MAX_VALUE;
    }

    @Override
    public BaseException format(Exception ex) {
        if (ex instanceof AccessDeniedException) {
            return new BaseException("不允许访问");
        }
        return null;
    }
}
