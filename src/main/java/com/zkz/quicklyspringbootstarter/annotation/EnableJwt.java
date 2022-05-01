package com.zkz.quicklyspringbootstarter.annotation;

import com.zkz.quicklyspringbootstarter.jwt.*;
import com.zkz.quicklyspringbootstarter.redis.RedisConfig;
import com.zkz.quicklyspringbootstarter.redis.RedisService;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableRedis
@Import({InterceptorConfig.class, JwtInterceptor.class, JwtSetting.class, TokenService.class,JwtUtils.class})
public @interface EnableJwt {
}
