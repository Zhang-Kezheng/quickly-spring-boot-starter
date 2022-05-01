package com.zkz.quicklyspringbootstarter.annotation;

import lombok.Data;

import java.lang.annotation.*;

@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Authorization {
}
