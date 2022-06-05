package com.zkz.quicklyspringbootstarter;

import com.zkz.quicklyspringbootstarter.config.SwaggerConfig;
import com.zkz.quicklyspringbootstarter.exception.GlobalExceptionHandlerRegistrar;
import com.zkz.quicklyspringbootstarter.log.LogAspectRegistrar;
import com.zkz.quicklyspringbootstarter.security.WebSecurityRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({WebSecurityRegistrar.class,
        GeneralBeanRegistrar.class,
        LogAspectRegistrar.class,
        SwaggerConfig.class,
        GlobalExceptionHandlerRegistrar.class})
@Documented
public @interface EnableMiddleware {

    /**
     * 启用接口日志记录
     */
    boolean enableLogAspect() default true;

    /**
     * 启用全局异常拦截
     * 仅能处理controller层.拦截器层等不能被处理
     */
    boolean enableGlobalExceptionHandler() default true;

    /**
     * 是否启用基于Springboot的接口安全验证
     */
    boolean enableWebSecurity() default true;
}
