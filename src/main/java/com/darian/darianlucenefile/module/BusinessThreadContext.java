package com.darian.darianlucenefile.module;

import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

public class BusinessThreadContext {

    /**
     * 时间戳
     */
    private long timeKey;

    /**
     * 是否为压测模式 上下文保留压测标记，在某些需要去压测标的场景finally中重新设置压测标
     */
    private boolean loadTest;

    /**
     * 压测uid
     */
    private String loadTestUid;

    /**
     * 扩展字段
     */
    private Map<String, String> extInfo;

    /**
     * 初始化函数
     */
    public BusinessThreadContext() {
        this.timeKey = System.currentTimeMillis();
    }

    /**
     * 对象拷贝
     *
     * @param context {@link BusinessThreadContext}
     * @return {@link BusinessThreadContext}
     */
    public static BusinessThreadContext cloneInstance(BusinessThreadContext context) {
        BusinessThreadContext newContext = new BusinessThreadContext();
        newContext.timeKey = context.getTimeKey();
        newContext.setLoadTest(context.isLoadTest());
        newContext.setLoadTestUid(context.getLoadTestUid());
        newContext.extInfo = new HashMap<>();
        if (!CollectionUtils.isEmpty(context.extInfo)) {
            //map中都是String，所以不存在浅拷贝的问题
            newContext.extInfo.putAll(context.extInfo);
        }
        return newContext;
    }

    public long getTimeKey() {
        return timeKey;
    }

    public boolean isLoadTest() {
        return loadTest;
    }

    public void setLoadTest(boolean loadTest) {
        this.loadTest = loadTest;
    }

    public String getLoadTestUid() {
        return loadTestUid;
    }

    public void setLoadTestUid(String loadTestUid) {
        this.loadTestUid = loadTestUid;
    }

    /**
     * 添加一个扩展属性
     *
     * @param key   属性 key
     * @param value 属性 value
     */
    public void addExtInfo(String key, String value) {
        if (extInfo == null) {
            extInfo = new HashMap<>();
        }
        if (key != null && key.length() > 0 && value != null && value.length() > 0) {
            extInfo.put(key, value);
        }
    }

    /**
     * 得到一个扩展属性
     *
     * @param key
     * @return String
     */
    public String getExtInfo(String key) {
        return (extInfo == null) ? null : extInfo.get(key);
    }
}