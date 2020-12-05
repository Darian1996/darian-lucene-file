package com.darian.darianlucenefile.exception;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a> 
 * @date 2020/4/13  3:51
 */
public class CustomerRuntimeException extends RuntimeException {
    public CustomerRuntimeException(String msg) {
        super(msg);
    }

    public CustomerRuntimeException(String msg, Exception e) {
        super(msg, e);
    }

}
