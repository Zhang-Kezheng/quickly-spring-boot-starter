package com.zkz.quicklyspringbootstarter.file;

import com.zkz.quicklyspringbootstarter.exception.MiddlewareException;
import com.zkz.quicklyspringbootstarter.log.LogUtils;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

public class ZipUtils {

    /**
     * 压缩文件
     *
     * @param fileNameInZip 可以为null或空，如果指定了在zip中的文件名字，则使用指定的名字。如果没有则使用原始名
     * @param file          待压缩的文件
     * @param zipName       指定zip文件的名字，如果未指定，则使用{uuid}.zip
     * @return 压缩文件的路径
     */
    public static Path zip(String fileNameInZip, @NotNull Path file, final String zipName) {
        // zip文件
        ZipFile zipFile = createZipFile(zipName);
        // 压缩
        zip(fileNameInZip, file, zipFile);
        return zipFile.getFile().toPath();
    }

    /**
     * 压缩文件到ZipFile中
     *
     * @param fileNameInZip 可以为null或空，如果指定了在zip中的文件名字，则使用指定的名字。如果没有则使用原始名
     * @param file          被压缩的文件
     * @param zipFile       zip文件
     */
    public static void zip(String fileNameInZip, @NotNull Path file, @NotNull ZipFile zipFile) {
        // 判断待压缩文件
        if (file == null) {
            throw new MiddlewareException("0250");
        }
        if (!Files.exists(file)) {
            throw new MiddlewareException("0251");
        }

        // 压缩时的参数
        ZipParameters parameters = new ZipParameters();
        if (StringUtils.isNotBlank(fileNameInZip)) {
            parameters.setFileNameInZip(fileNameInZip);
        }
        // 压缩
        try {
            if (Files.isDirectory(file)) {
                zipFile.addFolder(file.toFile(), parameters);
            } else {
                zipFile.addFile(file.toFile(), parameters);
            }
        } catch (ZipException e) {
            throw new MiddlewareException( e.getLocalizedMessage());
        }
    }


    /**
     * 将文件列表压缩成zip
     *
     * @param fileMap 文件列表。支持文件和文件夹。key即是fileNameInZip，value是file
     * @param zipName 指定zip文件的名字，如果未指定，则使用{uuid}.zip
     * @return 压缩的文件。默认储存在配置的缓存目录{@link FileCacheSetting#getDir()}
     */
    public static Path zip(@NotEmpty Map<String, File> fileMap, String zipName) {
        if (fileMap == null || fileMap.isEmpty()) {
            throw new MiddlewareException("0250");
        }

        ZipFile zipFile = createZipFile(zipName);
        fileMap.forEach((fileName, file) -> zip(fileName, file.toPath(), zipFile));
        return zipFile.getFile().toPath();
    }


    /**
     * 根据指定的zip文件名创建ZipFile实体类
     * <p>
     * 该方法会使用 {@link #prepareRandomDir()} 和 {@link #legalizeZipName(String)} 自动处理zip目录和文件名
     * 为了防止文件名冲突，这里会将文件放到一个随机子目录下。zip后缀名也会被合法化
     *
     * @param zipName zip的文件名
     * @return ZipFile实体类
     */
    private static ZipFile createZipFile(final String zipName) {
        // zip文件路径。为了防止文件名冲突，这里会将文件放到一个随机子目录下。zip后缀名也会被合法化
        Path zipPath = prepareRandomDir().resolve(legalizeZipName(zipName));
        return new ZipFile(zipPath.toFile());
    }

    /**
     * 准备随机的文件夹用于存放zip缓存文件
     * <p>
     * 如果遇到异常会自动重复3次，超过次数后抛异常
     */
    private static Path prepareRandomDir() {
        byte tryNum = 0;
        byte maxTryNum = 3;
        do {
            Path path = Paths.get(FileCacheUtils.getCacheDir(), UUID.randomUUID().toString());
            if (Files.exists(path)) {
                continue;
            }
            try {
                Files.createDirectories(path);
                return path;
            } catch (IOException e) {
                tryNum++;
                LogUtils.error("创建文件夹失败：" + path.toString());
                LogUtils.error(e.getLocalizedMessage());
            }
        } while (tryNum >= maxTryNum);
        // 次数过多时抛异常
        throw new MiddlewareException("0200");
    }

    /**
     * 将zip文件名合法化
     * <p>
     * 如果原本是null或空，则使用{uuid}.zip，如果原先的名字中不是.zip后缀，则会自动加上后缀
     *
     * @param zipName 原始文件名
     * @return 合法化后的文件名
     */
    private static String legalizeZipName(final String zipName) {
        final String extension = ".zip";
        if (StringUtils.isBlank(zipName)) {
            return UUID.randomUUID().toString() + extension;
        }
        if (zipName.endsWith(extension)) {
            return zipName;
        } else {
            return zipName + extension;
        }
    }
}
