package com.darian.darianlucenefile.module;

/***
 * 结果枚举类
 */
public enum PublicResultEnum implements IResultEnum {

    /**
     * 业务成功时
     */
    SUCCESS(200, "成功", ""),

    /**
     * 系统业务异常
     */
    SYSTEM_ERROR(500, "系统异常，请重试", "系统异常，请重试"),

    /**
     * 无效的参数
     */
    ILLEGAL_ARGUMENT_PARAMS(203, "无效的参数", "无效的参数"),
    ;

    /**
     * 结果码
     */
    private int resultCode;

    /**
     * 结果信息,展示给用户
     */
    private String resultMsg;

    /**
     * 异常信息,用于内部调试
     */
    private String errorMsg;

    /**
     * 构造枚举
     *
     * @param resultCode 结果码
     * @param resultMsg  结果提示
     */
    PublicResultEnum(int resultCode, String resultMsg) {
        this(resultCode, resultMsg, resultMsg);
    }

    /**
     * 构造枚举
     *
     * @param resultCode 结果码
     * @param resultMsg  结果提示
     * @param errorMsg   错误提示
     */
    PublicResultEnum(int resultCode, String resultMsg, String errorMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
        this.errorMsg = errorMsg;
    }

    @Override
    public int getResultCode() {
        return resultCode;
    }

    @Override
    public String getResultMsg() {
        return resultMsg;
    }

    @Override
    public String getErrorMsg() {
        return errorMsg;
    }
}