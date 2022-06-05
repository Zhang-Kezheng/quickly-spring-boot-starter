package com.zkz.quicklyspringbootstarter.log;

import com.zkz.quicklyspringbootstarter.EnableMiddleware;
import com.zkz.quicklyspringbootstarter.utils.SpringContextUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

public class LogAspectRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        AnnotationAttributes attributes = SpringContextUtils.getAnnotationAttributes(annotationMetadata, EnableMiddleware.class);
        if (attributes == null) {
            return;
        }
        boolean enableLogAspect = attributes.getBoolean("enableLogAspect");
        if (enableLogAspect) {
            SpringContextUtils.register(beanDefinitionRegistry, LogAspect.class);
        }
    }
}
