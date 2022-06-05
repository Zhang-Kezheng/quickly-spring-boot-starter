package com.zkz.quicklyspringbootstarter.apiResponse;

import lombok.Data;

import java.io.File;

/**
 * 该类准备废弃，将于2020-06-09日后（两个月后）删除
 * <p>
 * 因为该类可以被LinkedHashMap等Map类代替。
 * 在可预见的未来该类不会出现更多的字段，且Map天然存在key去重的特性，因此Map更合适
 * （文件名的重复性应该由调用者解决，因为不同的调用者可能需要不同的去重方案）
 */
@Data
@Deprecated
public class ZipDownloadFile {
    private String rawName;
    private File file;
}
