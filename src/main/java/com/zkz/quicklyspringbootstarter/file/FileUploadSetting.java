package com.zkz.quicklyspringbootstarter.file;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashSet;


@Data
@Component
@ConfigurationProperties("middleware.file-upload")
public class FileUploadSetting {
    /**
     * 默认的合格文件后缀名列表
     */
    private final String[] defaultAllowExtensions = {"jpg", "png", "bmp", "jpeg", "gif",
            "txt", "rtf", "doc", "ppt", "xls", "docx", "pptx", "xlsx",
            "rar", "zip", "tar", "gz", "pdf", "psd"};

    /**
     * 是否需要验证文件后缀名
     * 默认开启. 如果要关闭,请确认对服务器的安全性有了充分了解.
     */
    private boolean validateExtensions = true;

    /**
     * 允许的程序后缀名
     */
    private HashSet<String> allowableExtensions = new HashSet<>();

    /**
     * 不允许的程序后缀名
     */
    private HashSet<String> notAllowableExtensions = new HashSet<>();

    /**
     * 文件保存的根目录。绝对路径。
     */
    private String rootDir = "/data/upload-file";

    /**
     * 文件保存的拓展路径。建议以项目名进行区分。
     * 如果要使用文件上传,则必须配置此项
     */
    private String projectSign;
}
