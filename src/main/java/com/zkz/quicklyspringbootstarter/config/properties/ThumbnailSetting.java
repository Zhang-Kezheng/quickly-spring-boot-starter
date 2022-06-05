package com.zkz.quicklyspringbootstarter.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "middleware.thumbnail")
public class ThumbnailSetting {

    /**
     * 压缩质量
     */
    private double quality = 0.8;
}
