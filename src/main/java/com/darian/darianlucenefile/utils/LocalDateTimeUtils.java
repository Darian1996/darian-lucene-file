package com.darian.darianlucenefile.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a>
 * @date 2020/11/21  下午4:05
 */
public class LocalDateTimeUtils {

    private static String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";

    public static String getNowString() {
        return LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern(yyyy_MM_dd_HH_mm_ss));
    }
}
