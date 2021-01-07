package com.darian.darianlucenefile.listener;

import com.darian.darianlucenefile.config.DarianGitRepConfig;
import com.darian.darianlucenefile.config.DarianLuceneConfig;
import com.darian.darianlucenefile.config.DarianTokenConfig;
import com.darian.darianlucenefile.constants.DocumentConstants;
import com.darian.darianlucenefile.repository.CustomerFileRepository;
import com.darian.darianlucenefile.repository.LuceneRepository;
import com.darian.darianlucenefile.service.CustomerFileService;
import com.darian.darianlucenefile.utils.MailUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a> 
 * @date 2021/1/7  21:45
 */
public class CustomerListener implements SpringApplicationRunListener, Ordered {

    private Logger LOGGER = LoggerFactory.getLogger(CustomerListener.class);

    private SpringApplication application;

    private String[] args;

    private long startTime;

    private long endTime;

    public CustomerListener(SpringApplication application, String[] args) {
        this.application = application;
        this.args = args;
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void starting() {
        LOGGER.info("CustomerListener#starting");
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
        LOGGER.info("CustomerListener#environmentPrepared");

    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        LOGGER.info("CustomerListener#contextPrepared");

    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
        LOGGER.info("CustomerListener#contextLoaded");

    }

    @Override
    public void started(ConfigurableApplicationContext applicationContext) {
        LOGGER.info("CustomerListener#started");

        LOGGER.info("[" + DarianGitRepConfig.class.getSimpleName() + "]:"
                + applicationContext.getBean(DarianGitRepConfig.class));
        LOGGER.info("[" + DarianTokenConfig.class.getSimpleName() + "]:"
                + applicationContext.getBean(DarianTokenConfig.class));
        LOGGER.info("[" + DarianLuceneConfig.class.getSimpleName() + "]:"
                + applicationContext.getBean(DarianLuceneConfig.class));


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
                LOGGER.error("[" + key + "]:" + e.getMessage() + "\n", e);
            }
        }

        LOGGER.info("所有的文件已经初始化 load 一边了");
        if (System.getProperty("os.name").contains("Windows")) {
            LOGGER.error("系统是 windows 系统，各种 sh 命令都将不会执行 ... ... ");
        }
    }

    @Override
    public void running(ConfigurableApplicationContext context) {
        LOGGER.info("CustomerListener#running");
        endTime = System.currentTimeMillis();

        long cost_s = (endTime - startTime) / 1000;
        long cost_ms = (endTime - startTime) % 1000;
        String application_start_cost = "系统启动成功总耗时：" + cost_s + " s " + cost_ms + " ms";
        LOGGER.info(application_start_cost);

        String nowString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        DocumentConstants.APPLICATION_START_TIME = nowString;
        String application_start_string = "系统的启动时间....{ " + nowString + " }";

        String server_port = context.getEnvironment().getProperty("server.port");

        LOGGER.info("http://localhost:" + server_port + "/index.html");
        LOGGER.info(application_start_string);

        MailUtils.sendMail("darian-lucene-file-webflux", application_start_string + "<br>" + application_start_cost);
    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {
        LOGGER.error("CustomerListener#failed", exception);

    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
