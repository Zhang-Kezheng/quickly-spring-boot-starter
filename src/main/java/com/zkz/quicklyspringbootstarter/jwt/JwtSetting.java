package com.zkz.quicklyspringbootstarter.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("middleware.jwt")
@Data
public class JwtSetting {
    private String secretKey;
    private Integer survivalMinutes = 14400;
    private String tokenClassName;
}
