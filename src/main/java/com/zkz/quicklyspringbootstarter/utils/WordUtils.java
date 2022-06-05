package com.zkz.quicklyspringbootstarter.utils;

import com.zkz.quicklyspringbootstarter.exception.BaseException;
import org.apache.commons.io.IOUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.*;

public class WordUtils {

    /**
     * 从流中获取docx文档
     * <p>
     * 注意: 此方法只支持docx格式的文档. 旧式doc格式不兼容
     */
    public static XWPFDocument getXWPFDocument(InputStream inputStream) {
        try {
            return new XWPFDocument(inputStream);
        } catch (IOException e) {
            throw new BaseException("0211");
        }
    }


    /**
     * 保存到文件
     * <p>
     * 此方法会关闭document, 会自动处理file流
     */
    public static void save(XWPFDocument document, File file) {
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            document.write(outputStream);
            document.close();

            IOUtils.closeQuietly(outputStream);
        } catch (FileNotFoundException e) {
            throw new BaseException("0202");
        } catch (IOException e) {
            throw new BaseException("0200");
        }
    }
}
