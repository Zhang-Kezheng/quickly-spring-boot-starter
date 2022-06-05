package com.zkz.quicklyspringbootstarter.utils;

import org.apache.commons.beanutils.BeanMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CsvUtils {
    // 分隔符
    private static final String separator = "\t";
    // 换行符
    private static final String lineBreak = "\n";


    public static String transformCollectionToCsvString(Collection<?> collection) {
        if (collection == null || collection.isEmpty()) {
            return null;
        }

        List<String> title = new ArrayList<>(); // 使用list的原因是保证value的顺序一致性
        StringBuffer content = new StringBuffer();
        for (Object o : collection) {
            BeanMap beanMap = new BeanMap();
            beanMap.setBean(o);
            if (title.isEmpty()) {
                beanMap.keySet().forEach(key -> {
                    String stringKey = key.toString();
                    title.add(stringKey);
                    content.append(stringKey).append(separator);
                });
                content.append(lineBreak);
            }

            title.forEach(key -> {
                Object value = beanMap.get(key);
                content.append(value.toString()).append(separator);
            });
            content.append(lineBreak);
        }
        return content.toString();
    }
}
