package com.darian.darianlucenefile.module;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TracerThreadPoolTaskExecutor extends ThreadPoolExecutor {
    public TracerThreadPoolTaskExecutor(int corePoolSize, int queueCapacity) {
        super(corePoolSize, corePoolSize, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(queueCapacity));
    }

    @Override
    public void execute(Runnable task) {
        BusinessThreadContext businessThreadContext = ContextUtil.cloneBusinessThreadContext();
        super.execute(() -> {
            BusinessThreadLocal.put(businessThreadContext);
            try {
                task.run();
            } finally {
                BusinessThreadLocal.clearRef();
            }
        });
    }
}