package com.darian.darianlucenefile.utils;

import java.util.ArrayList;
import java.util.List;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a> 
 * @date 2020/6/7  1:29
 */
public class ListUtils {
    public static <T> List<T> of(T... arrays) {
        List<T> list = new ArrayList<>();
        if (arrays == null || arrays.length == 0) {
            return list;
        }

        for (T item : arrays) {
            list.add(item);
        }

        return list;
    }
}
