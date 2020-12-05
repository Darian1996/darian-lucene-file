
package com.darian.darianlucenefile.module;

public interface IResultEnum {

    /**
     * 结果码
     *
     * @return int
     */
    int getResultCode();

    /**
     * 结果信息,展示给用户
     *
     * @return String
     */
    String getResultMsg();

    /**
     * 异常信息,用于内部调试
     *
     * @return String
     */
    String getErrorMsg();
}