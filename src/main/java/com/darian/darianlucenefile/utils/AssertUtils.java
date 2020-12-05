package com.darian.darianlucenefile.utils;

import com.darian.darianlucenefile.exception.AssertionException;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.function.Supplier;

/***
 *
 *
 * @author <a href="1934849492@qq.com">Darian</a>
 * @date 2020/1/19  8:25
 */
public class AssertUtils {

    public static void assertBlank(String param, String msg) {
        assertFalse(StringUtils.hasText(param), msg);
    }

    public static void assertNotNull(Object o, String msg) {
        assertTrue(Objects.nonNull(o), msg);
    }

    public static void assertNotBlank(String param, String msg) {
        assertTrue(StringUtils.hasText(param), msg);
    }

    /**
     * expresionSupplier.get() = false , throw AssertionException(msg)
     *
     * @param expresionSupplier
     * @param msg
     */
    public static void assertTrue(Supplier<Boolean> expresionSupplier, String msg) {
        assertFalse(!expresionSupplier.get(), msg);
    }

    /**
     * expresionSupplier.get() = true , throw AssertionException(msg)
     *
     * @param expresionSupplier
     * @param msg
     */
    public static void assertFalse(Supplier<Boolean> expresionSupplier, String msg) {
        assertFalse(expresionSupplier.get(), msg);
    }

    /**
     * expresion == false -> throw AssertionException(msg)
     *
     * @param expresion
     * @param msg
     */
    public static void assertTrue(boolean expresion, String msg) {
        assertFalse(!expresion, msg);
    }

    /**
     * expresion == true -> throw AssertionException(msg)
     *
     * @param expresion
     * @param msg
     */
    public static void assertFalse(boolean expresion, String msg) {
        if (expresion) {
            throw new AssertionException(msg);
        }
    }
}
