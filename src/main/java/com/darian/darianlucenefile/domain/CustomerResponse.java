package com.darian.darianlucenefile.domain;

import com.darian.darianlucenefile.config.DocumentContants;
import com.darian.darianlucenefile.config.LoggerContants;
import com.darian.darianlucenefile.utils.LocalDateTimeUtils;
import lombok.Data;
import org.slf4j.MDC;

import java.util.Date;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a> 
 * @date 2020/4/13  2:57
 */
@Data
public class CustomerResponse<T> {
    private String code;
    private String notifyMsg;
    private T dataBody;
    private String time;
    private String traceId;

    /**
     * 系统启动时间
     */
    private String applicationStartTime;

    public static <T> CustomerResponse<T> ok(String notifyMsg, T dataBody) {
        CustomerResponse<T> customerResponse = new CustomerResponse<>();
        customerResponse.setCode("200");
        customerResponse.setNotifyMsg(notifyMsg);
        customerResponse.setDataBody(dataBody);
        customerResponse.setTime(LocalDateTimeUtils.getNowString());
        customerResponse.setApplicationStartTime(DocumentContants.APPLICATION_START_TIME);
        customerResponse.setTraceId(MDC.get(LoggerContants.TRACE_ID_KEY));
        return customerResponse;
    }

    public static <T> CustomerResponse<T> ok(T dataBody) {
        return ok("success", dataBody);
    }

    public static <T> CustomerResponse<T> error(String errorMsg) {
        return error(errorMsg, null);
    }

    public static <T> CustomerResponse<T> error(String errorMsg, T dataBody) {
        CustomerResponse<T> customerResponse = new CustomerResponse<>();
        customerResponse.setCode("500");
        customerResponse.setNotifyMsg(errorMsg);
        customerResponse.setDataBody(dataBody);
        customerResponse.setTime(LocalDateTimeUtils.getNowString());
        customerResponse.setApplicationStartTime(DocumentContants.APPLICATION_START_TIME);
        customerResponse.setTraceId(MDC.get(LoggerContants.TRACE_ID_KEY));
        return customerResponse;
    }
}
