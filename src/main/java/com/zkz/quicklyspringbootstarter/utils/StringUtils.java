package com.zkz.quicklyspringbootstarter.utils;


import com.zkz.quicklyspringbootstarter.exception.MiddlewareException;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils extends org.apache.commons.lang3.StringUtils {
    /**
     * 在使用正则表达式时，利用好其预编译功能，可以有效加快正则匹配速度
     */
    private static final Pattern IS_MOBILE_PATTERN = Pattern.compile("^[1]\\d{10}$");
    private static final Pattern IS_EMAIL_PATTERN = Pattern.compile("^[a-z0-9]+([._\\\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$");
    private static final Pattern IS_NUMBER_PATTERN = Pattern.compile("^[0-9]*$");
    private static final Pattern IS_ALL_NUMBER_PATTERN = Pattern.compile("^-?[0-9]+(\\.[0-9]+)?$");

    /**
     * 生成全小写，无-的uuid
     *
     * @return uuid
     */
    public static String generateUuid() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

    /**
     * 验证是否全是数字
     *
     * @param number 待验证字符
     * @return 验证结果。null，空，空格返回false。
     */
    public static Boolean isNumber(String number) {
        return regularMatch(number, IS_NUMBER_PATTERN);
    }

    /**
     * 验证是否是数字，包括负数、小数、数字很长时系统自动转为科学技术法的模式
     *
     * @param number 待验证字符
     * @return 验证结果。null，空，空格返回false。
     */
    public static Boolean isAllNumber(String number) {
        // 该正则表达式可以匹配所有的数字 包括负数
        try {
            new BigDecimal(number);
        } catch (Exception e) {
            return false;//异常 说明包含非数字。
        }
        return regularMatch(number, IS_ALL_NUMBER_PATTERN);
    }

    /**
     * 验证是否是11位的手机号码
     *
     * @param mobile 待验证字符
     * @return 验证结果。null，空，空格返回false。
     */
    public static Boolean isMobile(String mobile) {
        return regularMatch(mobile, IS_MOBILE_PATTERN);
    }

    /**
     * 验证邮箱格式
     *
     * @param email 待验证字符
     * @return 验证结果。null，空，空格返回false。
     */
    public static Boolean isEmail(String email) {
        return regularMatch(email, IS_EMAIL_PATTERN);
    }

    /**
     * 验证目标字符串和正则表达式是否匹配
     *
     * @param target  目标字符串
     * @param pattern 正则表达式
     * @return 验证结果。null，空，空格返回false。
     */
    public static Boolean regularMatch(String target, Pattern pattern) {
        if (org.apache.commons.lang3.StringUtils.isBlank(target)) {
            return false;
        }
        Matcher matcher = pattern.matcher(target);
        return matcher.matches();
    }

    /**
     * 验证是否是合法身份证
     */
    public static Boolean isIdCard(String idCard) {
        if (StringUtils.isBlank(idCard)) {
            throw new MiddlewareException("0430");
        }
        return IdCardUtils.isValidatedAllIdCard(idCard);
    }
}
