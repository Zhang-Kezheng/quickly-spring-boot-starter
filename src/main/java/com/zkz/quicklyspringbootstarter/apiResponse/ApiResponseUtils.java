package com.zkz.quicklyspringbootstarter.apiResponse;


import com.zkz.quicklyspringbootstarter.exception.BaseException;
import com.zkz.quicklyspringbootstarter.exception.MiddlewareException;
import com.zkz.quicklyspringbootstarter.file.ZipUtils;
import com.zkz.quicklyspringbootstarter.utils.httprequest.RequestUtils;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ApiResponseUtils {
    /**
     * 默认正常返回
     *
     * @return {@link SuccessResponse}
     */
    public static ApiResponse success() {
        return new SuccessResponse();
    }

    public static ApiResponse success(Object result) {
        return new SuccessResponse(result);
    }

    public static ApiResponse error(int code) {
        return new ErrorResponse(code);
    }

    public static ApiResponse error(int code, String extraMsg) {
        return new ErrorResponse(code, extraMsg);
    }

    public static ApiResponse error(BaseException baseException) {
        return new ErrorResponse(baseException);
    }


    /**
     * 返回文件流
     */
    public static ApiResponse fileStream(Path file, String downloadName) {
        // 如果是文件夹，则zip压缩
        if (Files.isDirectory(file)) {
            return zipFileStream(file, downloadName);
        }
        return fileStream(file.toFile(), downloadName);
    }

    /**
     * 返回文件流
     */
    public static ApiResponse fileStream(File file, String downloadName) {
        // 如果是文件夹，则zip压缩
        if (file.isDirectory()) {
            return zipFileStream(file.toPath(), downloadName);
        }

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            return fileStream(fileInputStream, downloadName);
        } catch (FileNotFoundException e) {
            throw new MiddlewareException("文件未找到");
        } catch (IOException e) {
            throw new BaseException("系统异常");
        }
    }

    /**
     * 返回文件流
     */
    public static ApiResponse fileStream(InputStream inputStream, String downloadName) {
        HttpServletResponse response = RequestUtils.getHttpServletResponse();
        setFileStreamHeader(response, downloadName);
        ServletOutputStream outputStream = null;

        try {
            outputStream = response.getOutputStream();
            IOUtils.copy(inputStream, outputStream);
        } catch (IOException e) {
            throw new BaseException("系统异常");
        } finally {
            IOUtils.closeQuietly(inputStream, outputStream);
        }
        return null;
    }


    /**
     * 返回zip的文件流
     *
     * @param file         下载的文件
     * @param downloadName 下载zip文件名
     * @return ApiResponse
     */
    public static ApiResponse zipFileStream(@NotNull Path file, @NotBlank final String downloadName) {
        Path zip = ZipUtils.zip(null, file, downloadName);
        return fileStream(zip, downloadName);
    }

    /**
     * 返回zip的文件流
     * <p>
     * 该方法已被废弃。将于2020-06-09日后（两个月后）删除。原因详见{@link ZipDownloadFile}。
     * 请使用 {@link #zipFileStream(Map, String)}
     *
     * @param downloadFileList 下载的文件类，里面包含file和rawName
     * @param downloadName     下载zip文件名
     * @return ApiResponse 因为是流，所以返回值是null
     */
    @Deprecated
    public static ApiResponse zipFileStream(@NotEmpty List<ZipDownloadFile> downloadFileList, @NotBlank final String downloadName) {
        Map<String, File> fileMap = new LinkedHashMap<>();
        downloadFileList.forEach(zipDownloadFile -> fileMap.put(zipDownloadFile.getRawName(), zipDownloadFile.getFile()));
        return zipFileStream(fileMap, downloadName);
    }

    /**
     * 返回zip的文件流
     * <p>
     * 请使用
     *
     * @param fileMap      下载的文件类，key是文件名，value是文件对象
     * @param downloadName 下载zip文件名
     * @return ApiResponse
     */
    public static ApiResponse zipFileStream(@NotEmpty Map<String, File> fileMap, @NotBlank String downloadName) {
        Path zip = ZipUtils.zip(fileMap, downloadName);
        return fileStream(zip, downloadName);
    }

    /**
     * 设置下载文件的返回头
     *
     * @param response     HttpServletResponse
     * @param downloadName 设置下载文件名
     */
    private static void setFileStreamHeader(HttpServletResponse response, String downloadName) {
        try {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + URLEncoder.encode(downloadName, StandardCharsets.UTF_8.name()));
        } catch (IOException e) {
            throw new MiddlewareException("系统异常");
        }
    }

    /**
     * 判断返回值是否有异常
     * 函数功能已经移动至 {@link ApiResponseParseUtils#isSuccess(String)}
     * 将于2020-06-09日后（两个月后）删除
     */
    @Deprecated
    public static boolean isSuccess(String response) {
        return ApiResponseParseUtils.isSuccess(response);
    }

    /**
     * 获取返回值中的数据
     * 函数功能已经移动至 {@link ApiResponseParseUtils#getData(String, Class)}
     * 将于2020-06-09日后（两个月后）删除
     */
    @Deprecated
    public static <T> T getData(String response, Class<T> clazz) throws MiddlewareException {
        return ApiResponseParseUtils.getData(response, clazz);
    }

    /**
     * 获取返回值中的错误信息
     * 函数功能已经移动至 {@link ApiResponseParseUtils#getError(String)}
     * 将于2020-06-09日后（两个月后）删除
     */
    @Deprecated
    public static ErrorResponse getError(String response) {
        return ApiResponseParseUtils.getError(response);
    }
}
