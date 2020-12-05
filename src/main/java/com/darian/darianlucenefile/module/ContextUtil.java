package com.darian.darianlucenefile.module;

public class ContextUtil {

    /**
     * 初始化上下文
     */
    public static void initContext() {
        BusinessThreadContext value = new BusinessThreadContext();
        BusinessThreadLocal.put(value);
    }

    /**
     * 复制上下文
     *
     * @return {@link BusinessThreadContext}
     */
    public static BusinessThreadContext cloneBusinessThreadContext() {
        BusinessThreadContext context = BusinessThreadLocal.getWithoutSet();
        if (context == null) {
            return null;
        }
        return BusinessThreadContext.cloneInstance(context);
    }

    /**
     * 是否为压测模式
     *
     * @return boolean
     */
    public static boolean isLoadTest() {
        BusinessThreadContext context = BusinessThreadLocal.getWithoutSet();
        if (context == null) {
            return false;
        }
        return Boolean.TRUE.equals(context.isLoadTest());
    }
}