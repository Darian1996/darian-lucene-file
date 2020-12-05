
package com.darian.darianlucenefile.repository;


import com.darian.darianlucenefile.domain.CustomerFile;
import com.darian.darianlucenefile.utils.AssertUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class CustomerFileRepository {


    private static final Map<String, CustomerFile> CUSTOMER_FILE_MAP = new HashMap<>();

    private static boolean INIT_FINISHED = false;

    /**
     * 标记 已经初始化完成
     */
    public void setInitFinshFinshed() {
        log.info(" CUSTOMER_FILE_MAP.size(): " + CUSTOMER_FILE_MAP.size());
        INIT_FINISHED = true;
    }

    /**
     * 获取所有文件的 MAP 集合的 Key
     *
     * @return
     */
    public List<String> getAllCustomerFileKeyList() {
        return new ArrayList<>(CUSTOMER_FILE_MAP.keySet());
    }

    /**
     * 根据 Key 返回一个 克隆对象
     *
     * @param filePathSubGitFilePath
     * @return
     */
    public CustomerFile findCopyCustomerFile(String filePathSubGitFilePath) {
        log.debug("[findCopyCustomerFile]: " + filePathSubGitFilePath);
        CustomerFile responseCustomerFile = new CustomerFile();
        CustomerFile customerFile = CUSTOMER_FILE_MAP.get(filePathSubGitFilePath);
        if (customerFile == null) {
            log.warn("content[" + filePathSubGitFilePath + "] 找不到");
            return null;
        }

        BeanUtils.copyProperties(customerFile, responseCustomerFile);
        return responseCustomerFile;
    }


    /**
     * 添加一个 CustomerFile
     *
     * @param filePathSubGitFilePath
     * @param customerFile
     */
    public void insertCustomerFile(String filePathSubGitFilePath, CustomerFile customerFile) {
        AssertUtils.assertFalse(INIT_FINISHED, "CustomerFileRepository is initFinished ... ");
        // 避免多线程用户使用这个方法，但是，单线程访问具有偏向锁，性能忽略不计
        synchronized (CUSTOMER_FILE_MAP) {
            CustomerFile put = CUSTOMER_FILE_MAP.put(filePathSubGitFilePath, customerFile);
            if (put != null) {
                log.error("有同路径的文件....");
                log.error("[putCustomerFile]--->>[filePathSubGitFilePath]", filePathSubGitFilePath, "[customerFileModule]", customerFile);
            }
        }
        return;
    }


}