package com.zkz.quicklyspringbootstarter.utils;

import com.zkz.quicklyspringbootstarter.exception.BaseException;
import com.zkz.quicklyspringbootstarter.log.LogUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class ExcelUtils {
    /**
     * 通过MultipartFile获取workbook
     * 处理掉异常和流
     */
    public static Workbook getWorkbook(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            return ExcelUtils.getWorkbook(inputStream);
        } catch (IOException e) {
            throw new BaseException("0204");
        }
    }

    /**
     * 通过文件路径获取workbook
     * 处理掉异常和流
     */
    public static Workbook getWorkbook(String filePath) {
        try (InputStream inputStream = new FileInputStream(filePath)) {
            return ExcelUtils.getWorkbook(inputStream);
        } catch (FileNotFoundException e) {
            throw new BaseException("0202");
        } catch (IOException e) {
            throw new BaseException("0204");
        }
    }

    /**
     * 解析excel文件流返回对于版本的workbook对象
     *
     * @param inputStream excel文件流，使用后会被关闭
     * @return workbook
     */
    public static Workbook getWorkbook(InputStream inputStream) {
        Workbook workbook;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            IOUtils.copy(inputStream, outputStream);
            workbook = new HSSFWorkbook(new ByteArrayInputStream(outputStream.toByteArray()));
        } catch (OfficeXmlFileException e) {
            try {
                workbook = new XSSFWorkbook(new ByteArrayInputStream(outputStream.toByteArray()));
            } catch (OfficeXmlFileException | IOException e1) {
                throw new BaseException( "文件格式错误");
            }
        } catch (IOException e) {
            throw new BaseException( "文件格式错误");
        }

        IOUtils.closeQuietly(inputStream);
        return workbook;
    }

    /**
     * 通过MultipartFile获取指定页数的sheet对象
     * 处理掉异常和流
     */
    public static Sheet getSheet(MultipartFile file, int sheetNum) {
        try (InputStream inputStream = file.getInputStream()) {
            return ExcelUtils.getSheet(inputStream, sheetNum);
        } catch (IOException e) {
            throw new BaseException("0204");
        }
    }

    /**
     * 解析excel文件返回第一个sheet对象
     *
     * @param inputStream 文件流
     * @return sheet对象
     */
    public static Sheet getSheet(InputStream inputStream) {
        return getSheet(inputStream, 0);
    }

    /**
     * 解析excel文件返回某一页的sheet对象
     *
     * @param inputStream 文件流
     * @param sheetNum    索引, 从0开始
     * @return sheet对象
     */
    public static Sheet getSheet(InputStream inputStream, int sheetNum) {
        Workbook workbook = ExcelUtils.getWorkbook(inputStream);
        if (workbook.getNumberOfSheets() < sheetNum + 1) {
            throw new BaseException("0205");
        }
        return workbook.getSheetAt(sheetNum);
    }

    /**
     * 保存工作簿到文件
     * <p>
     * 此方法会关闭workbook, 会自动处理file流
     */
    public static void save(Workbook workbook, File file) {
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
            workbook.close();

            IOUtils.closeQuietly(outputStream);
        } catch (FileNotFoundException e) {
            throw new BaseException("0202");
        } catch (IOException e) {
            throw new BaseException("0200");
        }
    }

    /**
     * csv 文本转换成excel
     */
    public static ByteArrayInputStream csvToExcel(String... csvStrings) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        HSSFWorkbook workbook = new HSSFWorkbook();
        Arrays.stream(csvStrings).filter(StringUtils::isNotBlank).forEach(csvString -> {
            HSSFSheet sheet = workbook.createSheet();
            String[] lines = csvString.split(CSVFormat.EXCEL.getRecordSeparator());
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i];
                if (StringUtils.isBlank(line)) {
                    continue;
                }
                HSSFRow row = sheet.createRow(i);
                String[] items = line.split(String.valueOf(CSVFormat.EXCEL.getDelimiter()));
                for (int j = 0; j < items.length; j++) {
                    row.createCell(j).setCellValue(items[j]);
                }
            }
        });
        try {
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {
            LogUtils.error("excel写入异常");
        }
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    /**
     * 返回表格的字节数据
     *
     * @param titles 表格第一行标题
     * @param params 表格内容
     */
    public static ByteArrayInputStream formatToXls(String[] titles, List<String[]> params) {

        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet();

        //设置表格标题
        Row titleRow = sheet.createRow(0);
        for (int i = 0, size = titles.length; i < size; i++) {
            Cell cell = titleRow.createCell(i);
            cell.setCellValue(titles[i]);
        }

        //设置表格内容
        for (int i = 0, size = params.size() + i; i < size; i++) {
            Row contentRow = sheet.createRow(i + 1);
            String[] content = params.get(i);
            for (int j = 0, length = content.length; j < length; j++) {
                Cell cell = contentRow.createCell(j);
                cell.setCellValue(content[j]);
            }
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {
            throw new BaseException("0407");
        }
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    /**
     * 返回表格的字节数据
     */
    public static ByteArrayInputStream formatToXls(List<String> titles, List<List<String>> params) {

        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet();

        //设置表格标题
        Row titleRow = sheet.createRow(0);
        for (int i = 0, size = titles.size(); i < size; i++) {
            Cell cell = titleRow.createCell(i);
            cell.setCellValue(titles.get(i));
        }

        //设置表格内容
        for (int i = 0, size = params.size() + i; i < size; i++) {
            Row contentRow = sheet.createRow(i + 1);
            List<String> content = params.get(i);
            for (int j = 0, length = content.size(); j < length; j++) {
                Cell cell = contentRow.createCell(j);
                cell.setCellValue(content.get(j));
            }
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {
            throw new BaseException("0407");
        }
        return new ByteArrayInputStream(outputStream.toByteArray());
    }
}
