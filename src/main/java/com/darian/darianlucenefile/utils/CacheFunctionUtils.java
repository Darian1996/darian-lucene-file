package com.darian.darianlucenefile.utils;

import java.util.function.Consumer;
import java.util.function.Supplier;

/***
 *
 *
 * @author <a href="1934849492@qq.com">Darian</a>
 * @date 2020/06/03  8:25
 */
public class CacheFunctionUtils {

    /**
     * 获取缓存的方法， Utils 类
     *
     * @param cache
     * @param cacheManagerGetFunction
     * @param notCacheManagerGet
     * @param cacheManagerSetFunction
     * @param <T>
     * @return
     */
    public static <T> T getCacheOrNotCache(boolean cache,
                                           Supplier<T> cacheManagerGetFunction,
                                           Supplier<T> notCacheManagerGet,
                                           Consumer<T> cacheManagerSetFunction) {
        if (cache) {
            T t = cacheManagerGetFunction.get();
            if (t != null) {
                return t;
            }
        }

        T t = notCacheManagerGet.get();

        if (cache) {
            cacheManagerSetFunction.accept(t);
        }
        return t;
    }
}