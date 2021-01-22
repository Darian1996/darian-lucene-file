package com.darian.darianlucenefile.filter;

import com.darian.darianlucenefile.config.DarianIpPortConfig;
import com.darian.darianlucenefile.filter.ip.IPContainer;
import com.darian.darianlucenefile.utils.AssertUtils;
import com.darian.darianlucenefile.utils.MailUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
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
 * @date 2020/11/24  下午7:33
 */
@Component
public class IPFilter implements WebFilter, EnvironmentAware, ApplicationContextAware {

    private static final Logger VISITOR_IP_MONITOR_LOGGER = LoggerFactory.getLogger("VISITOR_IP_MONITOR");


    private String[] activeProfiles;

    private DarianIpPortConfig darianIpPortConfig;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String realIpString = exchange.getRequest().getHeaders().getFirst("X-Real-IP");

        // dev 环境，去掉
        if (Stream.of(activeProfiles)
                .anyMatch(str -> Objects.equals(str, "dev") || Objects.equals(str, "devLinux"))
                || darianIpPortConfig.getWhiteIpNotSendEmail().equals(realIpString)) {
            return chain.filter(exchange);
        }

        AssertUtils.assertNotBlank(realIpString, "不是nginx转发的请求");

        AssertUtils.assertFalse(IPContainer.ILLEGAL_IP_SET.contains(realIpString), "IP 非法");

        if (!IPContainer.VISITOR_IP_SET.contains(realIpString)) {
            IPContainer.VISITOR_IP_SET.add(realIpString);

            String addIPLogging = "新增IP[" + realIpString + "]请确认IP的合法性";
            String allIPLogging = "全部的访问IP:[" + IPContainer.VISITOR_IP_SET + "]";
            String illegalIPLogging = "黑名单IP:[" + IPContainer.ILLEGAL_IP_SET + "]";

            VISITOR_IP_MONITOR_LOGGER.info(addIPLogging);
            VISITOR_IP_MONITOR_LOGGER.info(allIPLogging);
            VISITOR_IP_MONITOR_LOGGER.info(illegalIPLogging);

            MailUtils.sendMail("新的IP访问", addIPLogging + "<br>" + allIPLogging + "<br>" + illegalIPLogging);
        }

        return chain.filter(exchange);
    }

    @Override
    public void setEnvironment(Environment environment) {
        String[] environmentActiveProfiles = environment.getActiveProfiles();
        activeProfiles = environmentActiveProfiles;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        darianIpPortConfig = applicationContext.getBean(DarianIpPortConfig.class);
        AssertUtils.assertNotBlank(darianIpPortConfig.getWhiteIpNotSendEmail(), "darianIpPortConfig.whiteIpNotSendEmail 不能为空");
    }
}
