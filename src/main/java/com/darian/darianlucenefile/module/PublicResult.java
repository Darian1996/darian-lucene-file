package com.darian.darianlucenefile.module;

import java.io.Serializable;

public class PublicResult implements Serializable {

    private static final long serialVersionUID = 1810487101761618487L;

    /**
     * 标识是否成功
     */
    private boolean success = true;

    /**
     * resultCode, 结果编码, 使用 {@link PublicResultEnum}
     **/
    private int resultCode = PublicResultEnum.SUCCESS.getResultCode();

    /**
     * resultMsg, 结果描述, 使用 {@link PublicResultEnum}
     **/
    private String resultMsg = PublicResultEnum.SUCCESS.getResultMsg();

    public static PublicResult success() {
        return new PublicResult();
    }

    /**
     * 静态方法
     *
     * @param errorMessage errorMessage
     * @return {@link PublicResult}
     */
    public static PublicResult fail(String errorMessage) {
        PublicResult result = new PublicResult();
        result.setPublicResult(PublicResultEnum.SYSTEM_ERROR);
        result.setResultMsg(errorMessage);
        return result;
    }

    /**
     * 静态方法
     *
     * @param errorCode    errorCode
     * @param errorMessage errorMessage
     * @return {@link PublicResult}
     */
    public static PublicResult fail(int errorCode, String errorMessage) {
        PublicResult result = new PublicResult();
        result.setResultCode(errorCode);
        result.setResultMsg(errorMessage);
        // 既然是fail，那必须是success是false
        result.setSuccess(false);
        return result;
    }

    /**
     * 静态方法
     *
     * @param resultEnum {@link IResultEnum}
     * @return {@link PublicResult}
     */
    public static PublicResult fail(IResultEnum resultEnum) {
        PublicResult result = new PublicResult();
        result.setPublicResult(resultEnum);
        result.setSuccess(false);
        return result;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
        this.setSuccess(resultCode == 200);
    }

    public void setPublicResult(PublicResultEnum result) {
        this.resultCode = result.getResultCode();
        this.resultMsg = result.getResultMsg();
        this.setSuccess(resultCode == 200);
    }

    public void setPublicResult(IResultEnum result) {
        this.resultCode = result.getResultCode();
        this.resultMsg = result.getResultMsg();
        this.setSuccess(resultCode == 200);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setPublicResultMsg(PublicResultEnum result, String msg) {
        this.setPublicResult(result);
        this.resultMsg = msg;
    }
}