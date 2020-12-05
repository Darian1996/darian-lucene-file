package com.darian.darianlucenefile.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a> 
 * @date 2020/4/13  3:35
 */
@Slf4j
public class SemaphoreUtils {

    private static int semaphoreCount = Runtime.getRuntime().availableProcessors() * 2;

    private static Semaphore semaphore = new Semaphore(semaphoreCount);

    /**
     * 获得一个信号量，
     * true -> 获得
     * false -> 没有获得
     *
     * @return
     */
    public static boolean semaphoreLimit() {
        try {
            if (semaphore.hasQueuedThreads()) {
                return false;
            }
            boolean acquire = semaphore.tryAcquire(5, TimeUnit.MILLISECONDS);
            if (acquire) {
                log.debug("得到一把锁");
                return true;
            }

        } catch (Exception e) {
            log.error("争抢信号量失败，", e);
        }
        return false;
    }

    /**
     * 释放一个信号量
     */
    public static void release() {
        semaphore.release(1);
    }
}
