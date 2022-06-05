package com.zkz.quicklyspringbootstarter.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.text.MessageFormat;
import java.util.Arrays;

/**
 * 实现了{@link AbstractExceptionFormatter}
 * 默认的异常处理方法
 */
@Configuration
public class GeneralExceptionFormatter extends AbstractExceptionFormatter {
    @Override
    public int weight() {
        return Integer.MAX_VALUE;
    }

    @Override
    public BaseException format(Exception ex) {
        Throwable exCause = ex.getCause();

        if (ex instanceof BaseException) {
            return (BaseException) ex;
        }
        if (ex instanceof Errors) {
            FieldError fieldError = ((Errors) ex).getFieldErrors().get(0);
            String respMsg = MessageFormat.format("{0} {1}", fieldError.getField(), fieldError.getDefaultMessage());
            return new BaseException(respMsg);
        }
        if (ex instanceof HttpRequestMethodNotSupportedException) {
            String msg = "方法不支持"+((HttpRequestMethodNotSupportedException) ex).getMethod();
            return new BaseException( msg);
        }
        if (ex instanceof MaxUploadSizeExceededException) {
            MaxUploadSizeExceededException sizeException = (MaxUploadSizeExceededException) exCause.getCause();
            String displaySize = FileUtils.byteCountToDisplaySize(sizeException.getMaxUploadSize());
            String msg = String.format("文件最大不可超过 %s", displaySize);
            return new BaseException( msg);
        }
        if (ex instanceof HttpMessageNotReadableException) {
            if (exCause instanceof InvalidFormatException) {
                InvalidFormatException invalidFormatException = (InvalidFormatException) exCause;
                Object value = invalidFormatException.getValue();
                String simpleName = invalidFormatException.getTargetType().getSimpleName();
                String msg = value + " 不能转化成 " + simpleName + " 类型";
                return new BaseException( msg);
            }
        }
        if (ex instanceof NoHandlerFoundException){
            String msg = "路径不存在";
            return new BaseException( msg);
        }
        return new BaseException(ex.getMessage());
    }
}
