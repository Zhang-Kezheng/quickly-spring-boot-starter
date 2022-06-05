package com.zkz.quicklyspringbootstarter.utils;


import com.zkz.quicklyspringbootstarter.exception.MiddlewareException;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 数字处理工具类
 */
public class NumberUtils extends org.apache.commons.lang3.math.NumberUtils {
    // 默认精度
    private static final int DEFAULT_SCALE = 8;
    // 默认舍入模式
    private static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_UP;

    /**
     * BigDecimal除法，保留8位小数，采用RoundingMode.HALF_UP保留方式（四舍五入）
     *
     * @param numerator   分子
     * @param denominator 分母
     * @return 保留8位小数四舍五入后的商
     */
    public static BigDecimal divide(BigDecimal numerator, BigDecimal denominator) {
        return numerator.divide(denominator, DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
    }

    /**
     * 比较两个BigDecimal的值是否相等
     *
     * @param val1 参数1
     * @param val2 参数2
     * @return val1 == val2
     */
    public static Boolean equals(BigDecimal val1, BigDecimal val2) {
        return val1.compareTo(val2) == 0;
    }

    /**
     * 比较val1 < val2
     *
     * @param val1 参数1
     * @param val2 参数2
     * @return val1 < val2
     */
    public static Boolean lt(BigDecimal val1, BigDecimal val2) {
        return val1.compareTo(val2) < 0;
    }

    /**
     * 比较val1 <= val2
     *
     * @param val1 参数1
     * @param val2 参数2
     * @return val1 <= val2
     */
    public static Boolean lot(BigDecimal val1, BigDecimal val2) {
        return lt(val1, val2) || equals(val1, val2);
    }

    /**
     * 比较val1 > val2
     *
     * @param val1 参数1
     * @param val2 参数2
     * @return val1 > val2
     */
    public static Boolean gt(BigDecimal val1, BigDecimal val2) {
        return val1.compareTo(val2) > 0;
    }

    /**
     * 比较val1 >= val2
     *
     * @param val1 参数1
     * @param val2 参数2
     * @return val1 >= val2
     */
    public static Boolean got(BigDecimal val1, BigDecimal val2) {
        return gt(val1, val2) || equals(val1, val2);
    }

    /**
     * 该数据是否为0
     */
    public static Boolean isZero(BigDecimal val) {
        return equals(val, BigDecimal.ZERO);
    }

    /**
     * 该数据全部为0
     *
     * @param val 参数1
     * @return 全是0 -> true
     */
    public static Boolean isAllZero(BigDecimal... val) {
        for (BigDecimal item : val) {
            if (!isZero(item)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 该数据是否至少有一个为0
     *
     * @param val 参数1
     * @return 有一个是0 -> true
     */
    public static Boolean isContainZero(BigDecimal... val) {
        for (BigDecimal item : val) {
            if (isZero(item)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 取算术平均值
     *
     * @param numberList 原始数据集合
     * @return 如果参数为null或空, 返回0
     */
    public static BigDecimal getAverage(List<BigDecimal> numberList) {
        if (numberList == null || numberList.isEmpty()) {
            return BigDecimal.ZERO;
        }

        List<BigDecimal> rightNumberList = numberList.parallelStream().filter(Objects::nonNull).collect(Collectors.toList());
        if (rightNumberList.size() == 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal sum = rightNumberList.parallelStream().reduce(BigDecimal.ZERO, BigDecimal::add);
        return divide(sum, BigDecimal.valueOf(rightNumberList.size()));
    }

    /**
     * 获取最小值
     */
    public static BigDecimal getMin(List<BigDecimal> numberList) {
        if (numberList == null || numberList.isEmpty()) {
            throw new MiddlewareException("0450");
        }

        return numberList.parallelStream()
                .min(Comparator.naturalOrder())
                .orElseThrow(() -> new MiddlewareException("0451"));
    }

    /**
     * 获取最大值
     */
    public static BigDecimal getMax(List<BigDecimal> numberList) {
        if (numberList == null || numberList.isEmpty()) {
            throw new MiddlewareException("0450");
        }

        return numberList.parallelStream()
                .max(Comparator.naturalOrder())
                .orElseThrow(() -> new MiddlewareException("0451"));
    }


    /**
     * 判断data是否在interval区间内
     *
     * @param data     数据,为保证所有类型数据都能判断，请转为String类型
     * @param interval 正常的数学区间，包括无穷大等，如：(1,3)、(-∞,6]，支持百分号（符号都为英文符号否则会报错）
     * @return boolean
     */
    public static boolean isInTheInterval(String data, String interval) {
        //将区间和data转化为可计算的表达式
        String formula = getFormulaByInterval(data, interval, " && ");
        ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript");
        try {
            //计算表达式
            return (Boolean) jse.eval(formula);
        } catch (Exception t) {
            return false;
        }
    }

    /**
     * 将整个阀值区间转化为公式：如
     * 145)      =》         data < 145
     * [75,80)   =》        data < 80 && data >= 75
     *
     * @param data      数据
     * @param interval  形式如：145)、[75,80)
     * @param connector 连接符 如：&&
     */
    private static String getFormulaByInterval(String data, String interval, String connector) {
        StringBuilder sb = new StringBuilder();
        for (String halfInterval : interval.split(",")) {//如：[75,80)
            sb.append(getFormulaByHalfInterval(halfInterval, data)).append(connector);
        }
        String limitInterval = sb.toString();
        //去掉最后的连接符
        int index = limitInterval.lastIndexOf(connector);
        limitInterval = limitInterval.substring(0, index);
        return limitInterval;
    }

    /**
     * 将半个阀值区间转化为公式：如
     * 145)      =》         data < 145
     * [130      =》         data >= 130
     *
     * @param halfInterval 形式如：145)、[130
     * @param data         值
     * @return data < 145
     */
    private static String getFormulaByHalfInterval(String halfInterval, String data) {
        halfInterval = StringUtils.deleteWhitespace(halfInterval);

        if (halfInterval.contains("∞")) {//包含无穷大则不需要公式
            return "1 == 1";
        }
        StringBuilder sb = new StringBuilder();
        String num;
        String opera;
        if (halfInterval.matches("^([\\[\\(]{1}(-?\\d+.?\\d*\\%?))$")) {//表示判断方向（如[）在前面 如[80%
            opera = halfInterval.substring(0, 1);
            num = halfInterval.substring(1);
        } else {//145)
            opera = halfInterval.substring(halfInterval.length() - 1);
            num = halfInterval.substring(0, halfInterval.length() - 1);
        }
        double value = dealPercent(num);
        sb.append(data).append(" ").append(opera).append(" ").append(value);
        String a = sb.toString();
        //转化特定字符
        return a.replace("[", ">=").replace("(", ">").replace("]", "<=").replace(")", "<").replace("≤", "<=").replace("≥", ">=");
    }

    /**
     * 去除百分号，转为小数
     *
     * @param str 可能含百分号的数字
     */
    private static double dealPercent(String str) {
        double d;
        if (str.contains("%")) {
            str = str.substring(0, str.length() - 1);
            d = Double.parseDouble(str) / 100;
        } else {
            d = Double.parseDouble(str);
        }
        return d;
    }
}
