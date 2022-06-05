package com.zkz.quicklyspringbootstarter.file;

import com.zkz.quicklyspringbootstarter.exception.MiddlewareException;
import com.zkz.quicklyspringbootstarter.log.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FileCacheUtils {
    private static FileCacheSetting fileCacheSetting;

    @Autowired
    public FileCacheUtils(FileCacheSetting uploadFileProperties) {
        FileCacheUtils.fileCacheSetting = uploadFileProperties;
    }


    /**
     * 项目启动时执行的方法. 打印缓存目录
     */
    @PostConstruct
    public void prepareCacheDirectory() {
        LogUtils.info("缓存目录配置 " + getCacheDir());
    }

    /**
     * 获取配置中的缓存路径
     */
    public static String getCacheDir() {
        return fileCacheSetting.getDir();
    }

    /**
     * 创建缓存文件
     *
     * @param fileName 文件名
     * @param bytes    源文件bytes
     * @return 缓存文件的Path对象
     */
    public static Path createCache(String fileName, byte[] bytes) {
        Path filePathOfCache = getPathOfCacheFile(fileName);
        try {
            Files.write(filePathOfCache, bytes);
            return filePathOfCache;
        } catch (IOException e) {
            throw new MiddlewareException("文件缓存异常");
        }
    }


    /**
     * 将原始文件拷贝到缓存路径, 默认以UUID方式修改文件名
     * <p>
     * 考虑到缓存文件命名重复等因素，方法内会在遇到错误时重试三次，如果继续失败则抛异常
     *
     * @param originalFile 原始文件
     * @return 缓存路径下的新文件
     */
    public static Path copyToCache(final File originalFile) {
        prepareCacheDir();

        if (!originalFile.exists()) {
            throw new MiddlewareException("0221");
        }

        byte try_num = 0; // 尝试次数
        byte max_try_num = 3; // 最大尝试次数
        do {
            String uuidName = FileUploadUtils.createUUIDName(originalFile.getName());
            Path cachePath = Paths.get(getCacheDir(), uuidName);
            try {
                Files.copy(originalFile.toPath(), cachePath); // 如果文件已经存在会报错
                return cachePath;
            } catch (IOException e) {
                try_num++;
                LogUtils.warn(String.format("缓存文件创建失败%d/%d次：%s", try_num, max_try_num, cachePath.toString()));
            }
        } while (try_num >= max_try_num);

        // 超出重试次数后报错
        throw new MiddlewareException("0222");
    }

    /**
     * 获取缓存文件
     *
     * @param fileName 文件名
     * @return {@link Path}对象,缓存文件不一定实际存在
     */
    public static Path getCache(String fileName) {
        return getPathOfCacheFile(fileName);
    }

    /**
     * 获取缓存文件的具体路径(包含文件名的绝对路径，且包装成Path)
     *
     * @param fileName 文件名
     */
    private static Path getPathOfCacheFile(String fileName) {
        prepareCacheDir();
        return Paths.get(getCacheDir(), fileName);
    }

    /**
     * 如果缓存路径不存在则自动创建。
     * 由于某些情况下linux系统可能会自动清理/tmp文件夹内的文件，因此要保证每次创建文件都要确认文件夹已经存在
     */
    private static void prepareCacheDir() {
        String cacheDir = getCacheDir();
        Path path = Paths.get(cacheDir);
        // 已经存在时直接返回
        if (Files.isDirectory(path)) {
            return;
        }
        //创建缓存目录
        try {
            Files.createDirectories(path);
            LogUtils.info("新建缓存目录: " + cacheDir);
        } catch (IOException e) {
            LogUtils.error(String.format("创建临时目录 %s 异常: %s", cacheDir, e.getLocalizedMessage()));
        }
    }
}
