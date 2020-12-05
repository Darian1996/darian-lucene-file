package com.darian.darianlucenefile;

import com.darian.darianlucenefile.config.DocumentContants;
import com.darian.darianlucenefile.config.DarianGitRepConfig;
import com.darian.darianlucenefile.config.DarianLuceneConfig;
import com.darian.darianlucenefile.config.DarianTokenConfig;
import com.darian.darianlucenefile.repository.CustomerFileRepository;
import com.darian.darianlucenefile.repository.LuceneRepository;
import com.darian.darianlucenefile.service.CustomerFileService;
import com.darian.darianlucenefile.utils.MailUtils;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/***
 *
 *
 * @author <a href="1934849492@qq.com">Darian</a>
 * @date 2020/1/19  7:34
 */
@Slf4j
@SpringBootApplication
@EnableConfigurationProperties
public class DarianLuceneFileApplication {


    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        SpringApplication springApplication = new SpringApplication(DarianLuceneFileApplication.class);

        ConfigurableApplicationContext applicationContext = springApplication.run(args);

        init(applicationContext);

        String nowString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        DocumentContants.APPLICATION_START_TIME = nowString;

        String application_start_string = "系统的启动时间....{ " + nowString + " }";


        long end = System.currentTimeMillis();
        long cost_s = (end - start) / 1000;
        long cost_ms = (end - start) % 1000;

        String application_start_cost = "系统启动成功总耗时：" + cost_s + " s " + cost_ms + " ms";
        log.info(application_start_string);
        log.info(application_start_cost);

        MailUtils.sendMail("darian-lucene-file-webflux", application_start_string + "<br>" + application_start_cost);

    }

    /**
     * 初始化做一些操作
     *
     * @param applicationContext
     */
    public static void init(ConfigurableApplicationContext applicationContext) {
        log.info("[" + DarianGitRepConfig.class.getSimpleName() + "]:"
                + applicationContext.getBean(DarianGitRepConfig.class));
        log.info("[" + DarianTokenConfig.class.getSimpleName() + "]:"
                + applicationContext.getBean(DarianTokenConfig.class));
        log.info("[" + DarianLuceneConfig.class.getSimpleName() + "]:"
                + applicationContext.getBean(DarianLuceneConfig.class));

        String nowString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        DocumentContants.APPLICATION_START_TIME = nowString;
        log.info("系统的启动时间....{ " + nowString + " }");


        LuceneRepository luceneRepository = applicationContext.getBean(LuceneRepository.class);
        luceneRepository.createIndex();
        // 文件加载完毕以后，进行替换 图片的 链接

        luceneRepository.multiFiledQueryParser(".md");

        CustomerFileService customerFileService = applicationContext.getBean(CustomerFileService.class);
        CustomerFileRepository customerFileRepository = applicationContext.getBean(CustomerFileRepository.class);

        for (String key : customerFileRepository.getAllCustomerFileKeyList()) {
            try {
                customerFileService.doGetCustomerFile(key, false);
            } catch (Exception e) {
                log.error("[" + key + "]:" + e.getMessage() + "\n", e);
            }
        }

        log.info("所有的文件已经初始化 load 一边了");
        if (System.getProperty("os.name").contains("Windows")) {
            log.error("系统是 windows 系统，各种 sh 命令都将不会执行 ... ... ");
        }
    }

}