package com.zkz.quicklyspringbootstarter.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Component
public class SpringContextUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        SpringContextUtils.applicationContext = applicationContext;
    }


    /**
     * 根据类型获取已被注册的bean实例.包括子类
     */
    public static <T> Map<String, T> getBeansOfType(Class<T> aClass) {
        return applicationContext.getBeansOfType(aClass);
    }

    /**
     * 注册bean
     *
     * @param beanDefinitionRegistry beanDefinitionRegistry
     * @param someClass              对应bean的class
     */
    public static void register(BeanDefinitionRegistry beanDefinitionRegistry, Class... someClass) {
        for (Class aClass : someClass) {
            // bean定义
            GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition();
            genericBeanDefinition.setBeanClass(aClass);
            genericBeanDefinition.setSynthetic(true);

            // 生成驼峰命名风格的bean名
            String beanName = new AnnotationBeanNameGenerator().generateBeanName(genericBeanDefinition, beanDefinitionRegistry);
            // 进行注册
            beanDefinitionRegistry.registerBeanDefinition(beanName, genericBeanDefinition);
        }
    }

    /**
     * 获取注解的属性值列表
     */
    public static AnnotationAttributes getAnnotationAttributes(AnnotationMetadata annotationMetadata, Class aClass) {
        Map<String, Object> annotationAttributes = annotationMetadata.getAnnotationAttributes(aClass.getName());
        if (annotationAttributes == null) {
            return null;
        }
        return AnnotationAttributes.fromMap(annotationAttributes);
    }

}
