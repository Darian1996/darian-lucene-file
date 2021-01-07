package com.darian.darianlucenefile.domain;

import com.darian.darianlucenefile.constants.DocumentConstants;
import com.darian.darianlucenefile.constants.LoggerConstants;
import com.darian.darianlucenefile.utils.LocalDateTimeUtils;
import lombok.Data;
import org.slf4j.MDC;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a> 
 * @date 2020/4/13  2:57
 */
@Data
public class CustomerResponse<T, R> {
    private String code;
    private String notifyMsg;
    private T dataBody;
    private String time;
    private String traceId;

    /**
     * 系统启动时间
     */
    private String applicationStartTime;

    private R request;

    public static <T, R> CustomerResponse<T, R> ok(String notifyMsg, T dataBody) {
        CustomerResponse<T, R> customerResponse = new CustomerResponse<>();
        customerResponse.setCode("200");
        customerResponse.setNotifyMsg(notifyMsg);
        customerResponse.setDataBody(dataBody);
        customerResponse.setTime(LocalDateTimeUtils.getNowString());
        customerResponse.setApplicationStartTime(DocumentConstants.APPLICATION_START_TIME);
        customerResponse.setTraceId(MDC.get(LoggerConstants.TRACE_ID_KEY));
        return customerResponse;
    }

    public static <T, R> CustomerResponse<T, R> ok(T dataBody) {
        return ok("success", dataBody);
    }

    public static <T, R> CustomerResponse<T, R> error(String errorMsg) {
        return error(errorMsg, null);
    }

    public static <T, R> CustomerResponse<T, R> error(String errorMsg, T dataBody) {
        CustomerResponse<T, R> customerResponse = new CustomerResponse<>();
        customerResponse.setCode("500");
        customerResponse.setNotifyMsg(errorMsg);
        customerResponse.setDataBody(dataBody);
        customerResponse.setTime(LocalDateTimeUtils.getNowString());
        customerResponse.setApplicationStartTime(DocumentConstants.APPLICATION_START_TIME);
        customerResponse.setTraceId(MDC.get(LoggerConstants.TRACE_ID_KEY));
        return customerResponse;
    }
}
