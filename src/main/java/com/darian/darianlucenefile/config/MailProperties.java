package com.darian.darianlucenefile.config;

import com.darian.darianlucenefile.utils.MailUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Properties;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a> 
 * @date 2020/5/22  2:27
 */
@Data
@Slf4j
@Configuration
@ConfigurationProperties("mail")
@PropertySource({"classpath:/mail.properties"})
public class MailProperties implements InitializingBean {
    private String to;
    private String username;
    private String token;
    private String host;
    private String transport;
    private String smtp_auth;
    private String smtp_port;
    private String smtp_ssl_enable;
    private String debug;

    @Override
    public void afterPropertiesSet() throws Exception {
        Properties properties = new Properties();
        properties.setProperty("mail.to", to);
        properties.setProperty("mail.username", username);
        properties.setProperty("mail.token", token);
        properties.setProperty("mail.host", host);
        properties.setProperty("mail.transport", transport);
        properties.setProperty("mail.smtp.auth",smtp_auth );
        properties.setProperty("mail.smtp.port", smtp_port);
        properties.setProperty("mail.smtp.ssl.enable", smtp_ssl_enable);
        properties.setProperty("mail.debug", debug);

        MailUtils.properties = properties;
    }
}
