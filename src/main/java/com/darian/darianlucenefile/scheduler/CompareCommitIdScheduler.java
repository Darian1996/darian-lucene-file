package com.darian.darianlucenefile.scheduler;

import com.darian.darianlucenefile.utils.MailUtils;
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

    private static String DOCS_OLD_COMMIT_ID = ShellUtils.getDocsCommitId();

    private static String DARIAN_LUCENE_OLD_COMMIT_ID = ShellUtils.getDarianLuceneCommitId();

    /**
     * 每 1 分钟执行一次
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void scheduler() {
        String docsNewCommitId = ShellUtils.getDocsCommitId();
        String darianLuceneNewCommitId = ShellUtils.getDarianLuceneCommitId();
        String mailContent = null;
        String mailTitle = null;
        if (!DOCS_OLD_COMMIT_ID.equals(docsNewCommitId)) {
            mailTitle = "[docs][find_new_commit]";
            mailContent = mailTitle + String.format("[%s][TO][%s]", DOCS_OLD_COMMIT_ID, docsNewCommitId);
        }
        if (!DARIAN_LUCENE_OLD_COMMIT_ID.equals(darianLuceneNewCommitId)) {
            mailTitle = "[darian_lucene_file][find_new_commit]";
            mailContent = mailTitle + String.format("[%s][TO][%s]", DARIAN_LUCENE_OLD_COMMIT_ID, darianLuceneNewCommitId);
        }

        if (mailContent != null && mailContent.length() > 0) {
            MailUtils.sendMail(mailTitle, mailContent);
            log.info(mailContent);
            ShellUtils.thisAppplicationReStartSh();
        }

    }
}
