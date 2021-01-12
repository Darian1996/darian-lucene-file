package com.darian.darianlucenefile.service;

import com.darian.darianlucenefile.config.DarianGitRepConfig;
import com.darian.darianlucenefile.constants.DocumentConstants;
import com.darian.darianlucenefile.domain.CustomerFile;
import com.darian.darianlucenefile.exception.CustomerRuntimeException;
import com.darian.darianlucenefile.repository.CustomerFileRepository;
import com.darian.darianlucenefile.service.fileRead.FileReadService;
import com.darian.darianlucenefile.utils.AssertUtils;
import com.darian.darianlucenefile.utils.CacheFunctionUtils;
import com.darian.darianlucenefile.utils.CustomerFileUtils;
import com.darian.darianlucenefile.utils.CustomerImgUtils;
import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.googlecode.concurrentlinkedhashmap.Weighers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a> 
 * @date 2020/4/18  0:08
 */
@Slf4j
@Service
public class CustomerFileService {

    Map<String, CustomerFile> customerFileMap =
            new ConcurrentLinkedHashMap.Builder<String, CustomerFile>()
                    .maximumWeightedCapacity(5)
                    .weigher(Weighers.singleton())
                    .build();

    @Resource
    private CustomerFileRepository customerFileRepository;

    @Resource
    private FileReadService fileReadService;

    public CustomerFile doGetCustomerFile(String filePathSubDocsFilePath, boolean cache) {
        AssertUtils.assertTrue(StringUtils.hasText(filePathSubDocsFilePath), "filePathSubDocsFilePath不能为空");

        return CacheFunctionUtils.getCacheOrNotCache(cache,
                () -> customerFileMap.get(filePathSubDocsFilePath),
                () -> this.doGetCustomerFile(filePathSubDocsFilePath),
                value -> customerFileMap.put(filePathSubDocsFilePath, value));
    }

    private CustomerFile doGetCustomerFile(String filePathSubDocsFilePath) {
        AssertUtils.assertTrue(StringUtils.hasText(filePathSubDocsFilePath), "filePathSubDocsFilePath不能为空");
        CustomerFile responseFile = customerFileRepository.findCopyCustomerFile(filePathSubDocsFilePath);
        AssertUtils.assertTrue(Objects.nonNull(responseFile), "[" + filePathSubDocsFilePath + "]文章找不到");
        responseFile.setContentDetail(fileReadService.read(responseFile));
        return responseFile;
    }
}
