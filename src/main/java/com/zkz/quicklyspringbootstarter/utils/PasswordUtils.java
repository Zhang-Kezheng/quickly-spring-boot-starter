package com.zkz.quicklyspringbootstarter.utils;

import com.zkz.quicklyspringbootstarter.exception.MiddlewareException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 基于spring security的密码加密工具类
 * 密码加密采用BCrypt强哈希方法.(相比于MD5)此算法每次加密获取的密文都不同,可以大大降低数据库被脱裤的风险.
 */
public class PasswordUtils {
    private static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    //密码最少为6位
    private static final int MIN_LENGTH = 6;

    /**
     * 密码的先前验证
     */
    private static void preValidate(CharSequence rawPassword) {
        if (StringUtils.isBlank(rawPassword)) {
            throw new MiddlewareException("0410");
        } else if (rawPassword.length() < MIN_LENGTH) {
            throw new MiddlewareException("0411");
        }
    }

    /**
     * 密码加密
     * BCrypt强哈希方法
     */
    public static String encode(String rawPassword) {
        preValidate(rawPassword);
        return encoder.encode(rawPassword);
    }

    public static boolean matches(CharSequence rawPassword, String encodedPassword) {
        preValidate(rawPassword);
        if (StringUtils.isBlank(encodedPassword)) {
            throw new MiddlewareException("0412");
        }

        return encoder.matches(rawPassword, encodedPassword);
    }
}
