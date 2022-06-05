package com.zkz.quicklyspringbootstarter.file;

import com.zkz.quicklyspringbootstarter.exception.MiddlewareException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

@Component
public class FileUploadUtils {
    private static FileUploadSetting fileUploadSetting;

    @Autowired
    public void setFileUploadSetting(FileUploadSetting fileUploadSetting) {
        FileUploadUtils.fileUploadSetting = fileUploadSetting;
    }


    /**
     * 将流保存为文件
     * 如果不在配置里指定文件保存根目录则默认为"/data/upload-file"
     *
     * @param inputStream 文件流, 此方法会关闭流
     * @param fileName    要保持完整的文件名,带后缀.
     *                    可以携带相对路径. 如"corp-233/abc.jpg". 此时会放到"{rootDir}/{projectSign}/corp-233/abc.jpg"下.
     * @return String 文件保存路径
     */
    public static String saveFile(InputStream inputStream, String fileName) {
        // 判断projectSign是否配置
        String projectSign = fileUploadSetting.getProjectSign();
        if (StringUtils.isBlank(projectSign)) {
            throw new MiddlewareException("0201");
        }

        // 判断是否需要验证后缀名
        if (!fileUploadSetting.isValidateExtensions()) {
            validateExtension(fileName);
        }

        //文件全路径
        String targetPath = String.format("%s/%s/%s", fileUploadSetting.getRootDir(), fileUploadSetting.getProjectSign(), fileName);
        File targetFile = new File(targetPath);
        try {
            //创建目标文件
            FileUtils.touch(targetFile);
            //将上传的文件拷贝到目标文件
            FileUtils.copyInputStreamToFile(inputStream, targetFile);
            return targetPath;
        } catch (IOException e) {
            throw new MiddlewareException("0200");
        }
    }

    /**
     * 验证后缀名是否合法
     *
     * @param fileName 文件名
     * @throws MiddlewareException 文件名验证不通过会报错
     */
    private static void validateExtension(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            throw new MiddlewareException("0420");
        }

        //拿到后缀名
        String extension = FilenameUtils.getExtension(fileName);

        // 需要验证时,首先判断是否为空或null
        if (StringUtils.isBlank(extension)) {
            throw new MiddlewareException("0421");
        }

        //如果没有设置后缀名,则认为不需要验证
        HashSet<String> allowableExtensions = getAllowableExtensions();
        boolean contains = allowableExtensions.contains(extension);
        if (!contains) {
            throw new MiddlewareException("0422");
        }
    }

    /**
     * 获取所有的可用后缀名
     */
    private static HashSet<String> getAllowableExtensions() {
        // 获得所有的允许后缀
        String[] defaultAllowExtensions = fileUploadSetting.getDefaultAllowExtensions();
        HashSet<String> allowableExtensions = fileUploadSetting.getAllowableExtensions();
        allowableExtensions.addAll(Arrays.asList(defaultAllowExtensions));

        // 去除所有的不允许后缀
        HashSet<String> notAllowableExtensions = fileUploadSetting.getNotAllowableExtensions();
        for (String notAllowableExtension : notAllowableExtensions) {
            allowableExtensions.remove(notAllowableExtension);
        }
        return allowableExtensions;
    }

    /**
     * 获取文件的MD5
     *
     * @param inputStream 文件的流, 此方法会自动关闭流
     */
    public static String getMD5(InputStream inputStream) {
        try {
            return DigestUtils.md5DigestAsHex(inputStream);
        } catch (IOException e) {
            throw new MiddlewareException("0200");
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    /**
     * 生成文件uuid名
     */
    public static String createUUIDName(String filename) {
        String extension = FilenameUtils.getExtension(filename);
        if (extension == null) {
            extension = "";
        } else {
            extension = "." + extension;
        }

        String rawUUID = StringUtils.remove(UUID.randomUUID().toString(), "-");
        return rawUUID + extension;
    }
}
