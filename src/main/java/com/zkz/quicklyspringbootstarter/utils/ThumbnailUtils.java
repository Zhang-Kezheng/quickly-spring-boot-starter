package com.zkz.quicklyspringbootstarter.utils;

import com.zkz.quicklyspringbootstarter.config.properties.ThumbnailSetting;
import com.zkz.quicklyspringbootstarter.exception.MiddlewareException;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@Component
public class ThumbnailUtils {
    private static ThumbnailSetting thumbnailSetting;

    @Autowired
    public void setThumbnailProperties(ThumbnailSetting thumbnailSetting) {
        ThumbnailUtils.thumbnailSetting = thumbnailSetting;
    }

    /**
     * 生成缩略图,保存到文件
     * 如果宽或高为null,则按原图的宽或高处理
     *
     * @param width      目标宽度
     * @param height     目标高度
     * @param sourcePath 源文件
     * @param targetPath 目标文件
     */
    public static void createThumbnail(Integer width, Integer height, Path sourcePath, Path targetPath) {
        Thumbnails.Builder<File> fileBuilder = Thumbnails.of(sourcePath.toFile());

        //处理宽高
        if (width != null) {
            fileBuilder = fileBuilder.width(width);
        }
        if (height != null) {
            fileBuilder = fileBuilder.height(height);
        }

        //处理质量
        fileBuilder = fileBuilder.outputQuality(thumbnailSetting.getQuality());

        //输出到文件
        try {
            fileBuilder.toFile(targetPath.toFile());
        } catch (IOException e) {
            throw new MiddlewareException("图片压缩异常");
        }
    }
}
