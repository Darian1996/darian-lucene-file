package com.darian.darianlucenefile.filter;

import com.darian.darianlucenefile.utils.SemaphoreUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.stream.Stream;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a> 
 * @date 2020/4/17  22:28
 */
@Slf4j
@Component
public class SemaphoreLimitFilter implements WebFilter, EnvironmentAware {

    private String[] activeProfiles;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        // dev 环境，去掉
        if (Stream.of(activeProfiles)
                .anyMatch(str ->
                        Objects.equals(str, "dev") || Objects.equals(str, "devLinux"))) {
            return chain.filter(exchange);
        }

        // 获取锁
        boolean acquire = SemaphoreUtils.semaphoreLimit();
        if (acquire) {
            return chain.filter(exchange)
                    .doFinally(t -> {
                        log.debug("[SemaphoreLimitFilter.filter].....");
                        // 释放锁
                        SemaphoreUtils.release();
                    });
        } else {
            ServerHttpResponse response = exchange.getResponse();
            return ServerResponseFilterUtils.errorResponseWrapper(response, "Spring boot：触发信号量限流");
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        String[] environmentActiveProfiles = environment.getActiveProfiles();
        activeProfiles = environmentActiveProfiles;
    }
}
