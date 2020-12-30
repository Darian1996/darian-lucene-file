package com.darian.darianlucenefile.config;

import com.darian.darianlucenefile.service.IPPortsWhiteService;
import com.darian.darianlucenefile.utils.AssertUtils;
import com.darian.darianlucenefile.utils.ShellUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a> 
 * @date 2020/11/15  18:26
 */
@Slf4j
@Data
@Configuration
@ConfigurationProperties("darian.ipport.config")
public class DarianIpPortConfig implements InitializingBean {

    private String whitePort;


    @Override
    public void afterPropertiesSet() throws Exception {
        AssertUtils.assertNotBlank(whitePort, "whitePort is blank");
        List<Integer> portList = Stream.of(whitePort.split(","))
                .map(Integer::valueOf)
                .collect(Collectors.toList());

        ShellUtils.PORT_S = portList;
        IPPortsWhiteService.PORT_S = portList;
        log.info("portList:" + portList);
    }
}
