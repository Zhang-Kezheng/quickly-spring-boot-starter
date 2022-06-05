package com.zkz.quicklyspringbootstarter.utils;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import com.github.dozermapper.core.loader.api.BeanMappingBuilder;
import com.github.dozermapper.core.loader.api.TypeMappingOptions;
import com.zkz.quicklyspringbootstarter.exception.BaseException;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.commons.beanutils.BeanUtils.populate;

public class BeanMapperUtils {
    private static Mapper mapper = DozerBeanMapperBuilder.buildDefault();

    /**
     * 映射单个对象
     *
     * @param from    来源对象
     * @param toClass 目标类
     * @param <T>     任何类型
     * @return 目标类的实例化对象
     */
    public static <T> T map(Object from, Class<T> toClass) {
        return mapper.map(from, toClass);
    }

    /**
     * 映射单个对象
     *
     * @param from 来源对象
     * @param to   目标对象
     */
    public static void map(Object from, Object to) {
        mapper.map(from, to);
    }

    /**
     * 映射集合
     *
     * @param fromList 来源对象列表
     * @param toClass  目标类
     * @param <T>      任何类型
     * @return 目标类的实例化对象列表
     */
    public static <T> List<T> mapList(Collection fromList, Class<T> toClass) {
        if (fromList == null) {
            return null;
        }
        List<T> destinationList = new ArrayList<>(fromList.size());
        for (Object source : fromList) {
            destinationList.add(map(source, toClass));
        }
        return destinationList;
    }

    /**
     * 映射单个对象,值为null的字段不拷贝
     *
     * @param from 来源对象
     * @param to   目标对象
     */
    public static void mapWithoutNull(Object from, Object to) {
        BeanMappingBuilder builder = new BeanMappingBuilder() {
            @Override
            protected void configure() {
                mapping(from.getClass(), to.getClass(), TypeMappingOptions.mapNull(false));
            }
        };
        Mapper mapper = DozerBeanMapperBuilder.create().withMappingBuilder(builder).build();
        mapper.map(from, to);
    }

    /**
     * 映射单个对象,值为null的字段不拷贝
     *
     * @param from    来源对象
     * @param toClass 目标类
     * @param <T>     任何类型
     * @return 目标类的实例化对象
     */
    public static <T> T mapWithoutNull(Object from, Class<T> toClass) {
        if (from == null) {
            return null;
        }
        BeanMappingBuilder builder = new BeanMappingBuilder() {
            @Override
            protected void configure() {
                mapping(from.getClass(), toClass, TypeMappingOptions.mapNull(false));
            }
        };
        Mapper mapper = DozerBeanMapperBuilder.create().withMappingBuilder(builder).build();
        return mapper.map(from, toClass);
    }

    /**
     * 映射对象列表,值为null的字段不拷贝
     *
     * @param fromList 来源对象列表
     * @param toClass  目标类
     * @param <T>      任何类型
     * @return 目标类的实例化对象列表
     */
    public static <T> List<T> mapListWithoutNull(Collection fromList, Class<T> toClass) {
        if (fromList == null) {
            return null;
        }

        List<T> destinationList = new ArrayList<>(fromList.size());
        for (Object source : fromList) {
            T t = mapWithoutNull(source, toClass);
            destinationList.add(t);
        }
        return destinationList;
    }

    /**
     * 将key-value的Map集合转换成对象
     *
     * @param map         Map集合
     * @param targetClass 目标类
     * @param <T>         任何类型
     * @return 目标类的实例化对象
     */
    public static <T> T mapToBean(Map<String, Object> map, Class<T> targetClass) {
        try {
            T t = targetClass.newInstance();
            populate(t, map);
            return t;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new BaseException("0700");
        }
    }

    /**
     * 将key-value的Map集合转换成对象列表
     *
     * @param mapList     Map集合的列表
     * @param targetClass 目标类
     * @param <T>         任何类型
     * @return 目标类的实例化对象列表
     */
    public static <T> List<T> mapToBeanList(List<Map<String, Object>> mapList, Class<T> targetClass) {
        return mapList.stream().map(map -> mapToBean(map, targetClass)).collect(Collectors.toList());
    }
}
