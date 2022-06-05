package com.zkz.quicklyspringbootstarter.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "middleware.token")
public class TokenSetting {
    private String requestKey = "access-token";
}
