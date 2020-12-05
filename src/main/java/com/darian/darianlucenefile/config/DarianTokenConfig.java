package com.darian.darianlucenefile.config;

import com.darian.darianlucenefile.utils.AssertUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.StringUtils;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a> 
 * @date 2020/4/14  23:48
 */
@Data
@Slf4j
@Configuration
@ConfigurationProperties("darian.token.config")
public class DarianTokenConfig implements InitializingBean {

    private String token;

    @Override
    public void afterPropertiesSet() throws Exception {
        AssertUtils.assertTrue(StringUtils.hasText(token), "darian.token.config.token must not blank");

    }

    @Override
    public String toString() {
        return "\n" + "[DarianTokenConfig]" + "\n" +
                "{" + "\n" +
                "\t" + "token=\"" + token + "\"\n" +
                '}';
    }
}
