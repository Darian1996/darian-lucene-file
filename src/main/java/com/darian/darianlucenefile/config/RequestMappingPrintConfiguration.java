package com.darian.darianlucenefile.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.result.condition.PatternsRequestCondition;
import org.springframework.web.reactive.result.condition.RequestMethodsRequestCondition;
import org.springframework.web.reactive.result.method.RequestMappingInfo;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/***
 * print mapping
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a> 
 * @date 2020/4/12  21:44
 */
@Slf4j
@Component
public class RequestMappingPrintConfiguration implements ApplicationContextAware {
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        RequestMappingHandlerMapping requestMappingHandlerMapping = applicationContext.getBean(RequestMappingHandlerMapping.class);

        log.info("[CustomerApplicationContextAware] init");
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> requestMappingInfoHandlerMethodEntry : handlerMethods.entrySet()) {
            RequestMappingInfo requestMappingInfo = requestMappingInfoHandlerMethodEntry.getKey();
            HandlerMethod mappingInfoValue = requestMappingInfoHandlerMethodEntry.getValue();

            RequestMethodsRequestCondition methodCondition = requestMappingInfo.getMethodsCondition();

            PatternsRequestCondition patternsCondition = requestMappingInfo.getPatternsCondition();
            String requestUrl = patternsCondition.getPatterns().toString();

            String controllerName = mappingInfoValue.getBeanType().getSimpleName();
            String requestMethodName = mappingInfoValue.getMethod().getName();
            Class<?>[] methodParamTypes = mappingInfoValue.getMethod().getParameterTypes();
            String paramTypeString = Arrays.stream(methodParamTypes)
                    .map(Class::getSimpleName)
                    .collect(Collectors.joining(", "));

            log.info("[url]: " + requestUrl + "    [controller#method]: " + controllerName + "#" + requestMethodName);

        }
    }
}
