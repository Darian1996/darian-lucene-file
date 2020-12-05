package com.darian.darianlucenefile.filter;

import com.darian.darianlucenefile.config.DarianTokenConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;


/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a>
 * @date 2020/4/14  23:42
 */
@Component
public class LoginFilter implements WebFilter, ApplicationContextAware {

    private static Logger WEB_ACCESS_LOGGER = LoggerFactory.getLogger("WEB_ACCESS");

    private static Logger LOGGER = LoggerFactory.getLogger(LoginFilter.class);

    private static String TOKEN;

    private List<String> notNeedLoginPathList = Arrays.asList(
            "/index.html",
            "/contentDetail.html",
            "/favicon.ico",

            "/js/jquery-3.3.1.min.js",
            "/js/darian.own.js",
            "/js/sw-registration.js",
            "/js/marked.min.js",
            "/js/jquery.ztree.all-3.5.min.js",
            "/js/jquery.ztree_toc.min.js",
            "/js/highlight.min.js",
            "/sw.js",

            "/css/darian.own.css",
            "/css/zTreeStyle.css",
            "/css/github-markdown.css"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();

        String path = request.getPath().value();
        LOGGER.debug("[path]" + path);


        // 1. 不需要登录的地址 uri 2. 不是以 .html 结尾
        if (!notNeedLoginPathList.contains(path) && !path.endsWith(".html")) {

            WEB_ACCESS_LOGGER.info(getRequestInfo(request));

            if (!checkLogin(request)) {
                // 未授权
                ServerHttpResponse response = exchange.getResponse();
                return ServerResponseFilterUtils.errorResponseWrapper(response, "Spring boot：必须登录");
            }
        }


        return chain.filter(exchange)
                .doFinally(t -> LOGGER.debug("[LoginFilter.filter]....."));

    }

    private boolean checkLogin(ServerHttpRequest request) {

        List<String> tokenHeaderList = new ArrayList<>();

        MultiValueMap<String, HttpCookie> cookies = request.getCookies();
        if (cookies != null || !cookies.isEmpty()) {
            // 先从 cookie 里边取出来，判断
            List<HttpCookie> tokenCookieList = request.getCookies().get("token");

            if (tokenCookieList != null && !tokenCookieList.isEmpty()) {
                for (HttpCookie httpCookie : tokenCookieList) {
                    if ("token".equals(httpCookie.getName())) {
                        tokenHeaderList.add(httpCookie.getValue());
                    }
                }
            }
        }

        if (tokenHeaderList == null || tokenHeaderList.isEmpty()) {
            // cookie 里边没有值的话再从 header 里边来判断。
            tokenHeaderList = request.getHeaders().get("token");
        }

        if (tokenHeaderList == null || tokenHeaderList.isEmpty()) {
            LOGGER.debug("token is null");
            return false;
        }

        String requestToken = tokenHeaderList.get(0);
        LOGGER.debug("[requestToken]:" + requestToken);
        if (TOKEN.equals(requestToken)) {
            return true;
        }
        LOGGER.error("[requestURI]{" + request.getURI() + "}[requestToken]{" + requestToken + " }: equals false");
        return false;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        DarianTokenConfig darianTokenConfig = applicationContext.getBean(DarianTokenConfig.class);
        TOKEN = darianTokenConfig.getToken();
    }

    private static String getRequestInfo(ServerHttpRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append(" [requestId]: ").append(request.getId());
        sb.append(" [requestPath]: ").append(request.getPath());
        sb.append(" [requestQueryParams]: ").append(request.getQueryParams());
        sb.append(" [requestCookies]: ").append(request.getCookies());
        sb.append(" [requestHeaders]: ").append(getHeaderString(request.getHeaders()));
        sb.append(" [requestRemoteAddress]: ").append(request.getRemoteAddress());
        sb.append(" [requestSslInfo]: ").append(request.getSslInfo());
        sb.append(" [requestBody]: ").append(request.getBody());
        sb.append(" [requestMethod]: ").append(request.getMethod());
        sb.append(" [requestMethodValue]: ").append(request.getMethodValue());
        return sb.toString();
    }

    public static String getHeaderString(HttpHeaders headers) {
        String headerString = " {";

        for (Entry<String, List<String>> entry : headers.entrySet()) {
            headerString += "[";
            headerString += entry.getKey();
            headerString += "=";
            headerString += entry.getValue();
            headerString += "], ";
        }

        headerString += " } ";
        return headerString;
    }

}
