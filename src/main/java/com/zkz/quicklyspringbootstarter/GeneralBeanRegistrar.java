package com.zkz.quicklyspringbootstarter;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 对下面设置的包进行扫描,将其中加了Component等注解的类纳入spring管理
 */
@Configuration
@ComponentScan({"com.zkz.quicklyspringbootstarter.apiResponse",
        "com.zkz.quicklyspringbootstarter.config",
        "com.zkz.quicklyspringbootstarter.file",
        "com.zkz.quicklyspringbootstarter.jwt",
        "com.zkz.quicklyspringbootstarter.utils"})
public class GeneralBeanRegistrar {
}
