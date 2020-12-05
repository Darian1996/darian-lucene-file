package com.darian.darianlucenefile.scheduler;

import com.darian.darianlucenefile.utils.ShellUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;


/***
 * 定时任务
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a> 
 * @date 2020/7/30  20:35
 */
@Configuration
@EnableScheduling
@Slf4j
public class CompareCommitIdScheduler {

    private static String DOCS_LAST_COMMIT_ID = ShellUtils.getDocsCommitId();

    private static String DARIAN_LUCENE_LAST_COMMIT_ID = ShellUtils.getDarianLuceneCommitId();

    /**
     * 每 1 分钟执行一次
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void scheduler() {
        String docsCommitId = ShellUtils.getDocsCommitId();
        String darianLuceneCommitId = ShellUtils.getDarianLuceneCommitId();
        if (!DOCS_LAST_COMMIT_ID.equals(docsCommitId) || !DARIAN_LUCENE_LAST_COMMIT_ID.equals(darianLuceneCommitId)) {
            log.info("有新文件更新 ... ... ");
            ShellUtils.thisAppplicationReStartSh();
        }
    }
}
