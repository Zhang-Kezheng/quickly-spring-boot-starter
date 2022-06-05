package com.zkz.quicklyspringbootstarter.file;

import com.zkz.quicklyspringbootstarter.exception.MiddlewareException;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "middleware.file-cache")
public class FileCacheSetting {

    /**
     * 文件缓存路径
     */
    private String dir = "/tmp/file_cache";

    /**
     * 获取文件缓存路径的方法
     * <p>
     * 会自动去除末尾的"/"
     */
    public String getDir() {
        if (StringUtils.isBlank(dir)) {
            throw new MiddlewareException("0220");
        }
        return StringUtils.stripEnd(dir, "/");
    }
}
