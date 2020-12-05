package com.darian.darianlucenefile.config;

import com.darian.darianlucenefile.domain.CustomerResponse;
import com.darian.darianlucenefile.exception.AssertionException;
import com.darian.darianlucenefile.exception.CustomerRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebInputException;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a> 
 * @date 2020/4/13  2:51
 */
@ControllerAdvice
@Slf4j
@RestController
public class DarianControllerAdvice {

    @ExceptionHandler({AssertionException.class})
    public CustomerResponse onAssertionException(AssertionException e) {
        return CustomerResponse.error(e.getMessage());
    }

    @ExceptionHandler({ServerWebInputException.class})
    public CustomerResponse onServerWebInputException(ServerWebInputException e) {
        String reason = e.getReason();
        return CustomerResponse.error(reason);
    }

    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
    public CustomerResponse onERR_SERVICE_INPUT_VALIDATION_REJECTED(Exception e) {
        BindingResult bindingResult;
        if (e instanceof BindException) {
            bindingResult = BindException.class.cast(e).getBindingResult();
        } else {
            bindingResult = MethodArgumentNotValidException.class.cast(e).getBindingResult();
        }

        List<String> bindExceptionMessages = bindingResult.getAllErrors().stream()
                .map(objectError -> {
                    String errorCode = Stream.of(objectError.getCodes()).findFirst().get();
                    errorCode = errorCode.substring(errorCode.indexOf(".") + 1);
                    return "[" + errorCode + "]:" + objectError.getDefaultMessage();
                }).collect(Collectors.toList());

        return CustomerResponse.error("入参错误", bindExceptionMessages);
    }

    @ExceptionHandler(CustomerRuntimeException.class)
    public CustomerResponse onCustomerRuntimeException(CustomerRuntimeException e) {
        return CustomerResponse.error(e.getMessage());
    }

    @ExceptionHandler(Throwable.class)
    public CustomerResponse onThrowable(Throwable e) {
        if (e instanceof IOException && "你的主机中的软件中止了一个已建立的连接。".equals(e.getMessage())) {
            log.debug("浏览器断开链接.....");
        } else {
            log.error(e.getMessage(), e);
        }
        return CustomerResponse.error("服务器内部错误, 请联系管理员");
    }
}
