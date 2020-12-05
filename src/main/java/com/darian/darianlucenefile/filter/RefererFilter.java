package com.darian.darianlucenefile.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a>
 * @date 2020/4/14  22:39
 */
@Deprecated
@Slf4j
@Component
public class RefererFilter implements WebFilter {

    private List<String> refererStringList = Arrays.asList(
            "http://localhost",
            "http://127.0.0.1",
            "https://39.105.143.0/",
            "https://darian1996.github.io/",
            "https://darian1996.gitee.io/",
            "http://darian1996.gitee.io/",
            "https://darian.top/",
            "https://www.darian.top/");

    private List<String> notNeedRefererPathList = Arrays.asList(
            "/",
            "/index.html",
            "/favicon.ico",
            "/contentDetail.html",
            "/port.html",
            "/email.html");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        // 过滤器不使用了
        if (true) {
            return chain.filter(exchange);
        }

        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders httpHeaders = request.getHeaders();
        ServerHttpResponse response = exchange.getResponse();

        String path = request.getPath().value();
        log.debug("[path]" + path);
        if (!notNeedRefererPathList.contains(path) && !path.endsWith(".html")) {
            if (!checkReferer(httpHeaders)) {

                // 未授权
                return ServerResponseFilterUtils.errorResponseWrapper(response, "Spring boot：防盗链验证错误");
            }
        }

        return chain.filter(exchange)
                .doFinally(t -> log.debug("[RefererFilter.filter]....."));
    }


    public boolean checkReferer(HttpHeaders httpHeaders) {
        List<String> refererHeaderList = httpHeaders.get("Referer");
        if (refererHeaderList == null || refererHeaderList.isEmpty()) {
            log.debug("Referer is null");
            return false;
        }

        String requestReferer = refererHeaderList.get(0);
        log.debug("[requestReferer]:" + requestReferer);

        String finalRequestReferer = requestReferer;
        boolean present = refererStringList.stream()
                .filter(str -> finalRequestReferer.startsWith(str))
                .findAny()
                .isPresent();

        if (!present) {
            log.error("[requestReferer]" + requestReferer + "-- check false");
        }

        return present;
    }

}

