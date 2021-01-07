package com.darian.darianlucenefile;

import com.darian.darianlucenefile.config.MailProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.PropertySource;


/***
 *
 *
 * @author <a href="1934849492@qq.com">Darian</a>
 * @date 2020/1/19  7:34
 */
@SpringBootApplication
@EnableConfigurationProperties(MailProperties.class)
@PropertySource({"classpath:/META-INF/spring/mail.properties"})
public class DarianLuceneFileApplication {


    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(DarianLuceneFileApplication.class);
        ConfigurableApplicationContext applicationContext = springApplication.run(args);
    }
}