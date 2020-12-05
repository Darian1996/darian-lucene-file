package com.darian.darianlucenefile.filter;

import com.darian.darianlucenefile.domain.CustomerResponse;
import com.darian.darianlucenefile.utils.JSONUtils;
import com.darian.darianlucenefile.utils.ListUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a> 
 * @date 2020/6/7  1:36
 */
@Slf4j
public class ServerResponseFilterUtils {

    public static Mono<Void> errorResponseWrapper(ServerHttpResponse response, String msg) {
        String responseMsg = JSONUtils.beanToJson(CustomerResponse.error(msg));
        response.getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        return response
                .writeWith(Mono.just(responseMsg)
                        .map(bx -> response.bufferFactory().wrap(bx.getBytes())))
                .doFinally(t -> log.debug(msg));

    }
}
