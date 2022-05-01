package com.zkz.quicklyspringbootstarter.annotation;

import com.zkz.quicklyspringbootstarter.redis.RedisConfig;
import com.zkz.quicklyspringbootstarter.redis.RedisService;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({RedisConfig.class, RedisService.class})
public @interface EnableRedis {

}
