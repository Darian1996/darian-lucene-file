package com.darian.darianlucenefile.module;

public class BusinessThreadLocal {

    /**
     * 本地业务线程
     */
    private static final ThreadLocal<BusinessThreadContext> LOCAL_CONTEXT = new ThreadLocal<BusinessThreadContext>();

    /**
     * 获得一个当前线程业务变量，如果没有的话，返初始化的 {@link BusinessThreadContext}
     *
     * @return {@link BusinessThreadContext}
     */
    public static BusinessThreadContext get() {
        if (null == LOCAL_CONTEXT.get()) {
            return getWithReset();
        }
        return LOCAL_CONTEXT.get();
    }

    /**
     * 重置后获取一般用于入口处，进行一个上下文的重置，防止后面使用到错误的数据</p>
     *
     * @return {@link BusinessThreadContext}
     */
    private static BusinessThreadContext getWithReset() {
        BusinessThreadContext context = new BusinessThreadContext();
        LOCAL_CONTEXT.set(context);
        return LOCAL_CONTEXT.get();
    }

    /**
     * 获得一个当前线程业务变量,没有则返回 null
     *
     * @return {@link BusinessThreadContext}
     */
    public static BusinessThreadContext getWithoutSet() {
        return LOCAL_CONTEXT.get();
    }

    /**
     * 将当前业务上下文变量放入本地线程中
     *
     * @param context {@link BusinessThreadContext}
     */
    public static void put(BusinessThreadContext context) {
        if (context != null) {
            LOCAL_CONTEXT.set(context);
        }
    }

    /**
     * 手动释放内存，注意该方法应该在嵌套事务的最外层使用，否则会造成变量提前被清除的问题。
     */
    public static void clearRef() {
        LOCAL_CONTEXT.remove();
    }
}