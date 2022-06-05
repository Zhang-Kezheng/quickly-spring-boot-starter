package com.zkz.quicklyspringbootstarter.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT配置
 */
@Data
@Component
@ConfigurationProperties("middleware.jwt")
public class JwtSetting {
    /**
     * JWT秘钥.
     * 不同项目请设置不同秘钥
     */
    private String secretKey;

    /**
     * token有效期
     * 默认14400分钟(10*24小时,10天)
     * 如果设置为0或者负数,则永久生效
     */
    private Integer survivalMinutes = 14400;
}
