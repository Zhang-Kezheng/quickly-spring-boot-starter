package com.zkz.quicklyspringbootstarter.security;

import com.zkz.quicklyspringbootstarter.EnableMiddleware;
import com.zkz.quicklyspringbootstarter.utils.SpringContextUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

public class WebSecurityRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(@NotNull AnnotationMetadata annotationMetadata, @NotNull BeanDefinitionRegistry beanDefinitionRegistry) {
        AnnotationAttributes attributes = SpringContextUtils.getAnnotationAttributes(annotationMetadata, EnableMiddleware.class);
        if (attributes == null) {
            return;
        }
        boolean enableWebSecurity = attributes.getBoolean("enableWebSecurity");
        if (enableWebSecurity) {
            SpringContextUtils.register(beanDefinitionRegistry, Importer.class);
        }
    }

    /**
     * 扫描{@link ComponentScan}所配置的包中的所有{@link Component}注解,
     * 将带有此注解的类纳入spring bean管理
     */
    @Configuration
    @ComponentScan({"com.zkz.quicklyspringbootstarter.security"})
    public static class Importer {
    }
}
