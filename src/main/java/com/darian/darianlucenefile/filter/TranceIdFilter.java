package com.darian.darianlucenefile.filter;

import com.darian.darianlucenefile.constants.LoggerConstants;
import org.slf4j.MDC;
import org.springframework.boot.web.reactive.filter.OrderedWebFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.UUID;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a>
 * @date 2020/11/23  上午2:52
 */
@Component
public class TranceIdFilter implements OrderedWebFilter {
    private String TraceId_log_key = LoggerConstants.TRACE_ID_KEY;

    @Override
    public int getOrder() {
        return OrderedWebFilter.HIGHEST_PRECEDENCE;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String tranceIdValue = exchange.getRequest().getHeaders().getFirst(TraceId_log_key);
        if (StringUtils.isEmpty(tranceIdValue)) {
            tranceIdValue = UUID.randomUUID().toString().replaceAll("-", "");
        }

        MDC.put(TraceId_log_key, tranceIdValue);
        return chain.filter(exchange);
    }
}
