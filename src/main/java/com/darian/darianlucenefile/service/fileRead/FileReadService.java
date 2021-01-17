package com.darian.darianlucenefile.service.fileRead;

import com.darian.darianlucenefile.domain.CustomerFile;
import com.darian.darianlucenefile.utils.AssertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class FileReadService {

    private Logger LOGGER = LoggerFactory.getLogger(FileReadService.class);

    @Resource
    private List<FileReadProcessor> fileReadProcessorList;

    public String read(CustomerFile customerFile) {
        AssertUtils.assertNotBlank(customerFile.getFileSimpleName(), "文件[fileSimpleName]不能为空");
        AssertUtils.assertNotNull(fileReadProcessorList, "处理器列表为空");

        for (FileReadProcessor fileReadProcessor : fileReadProcessorList) {
            if (fileReadProcessor.supportProcessor(customerFile)) {
                long start = System.currentTimeMillis();

                String processName = fileReadProcessor.getClass().getSimpleName();

                String fileSimpleName = customerFile.getFileSimpleName();

                boolean success = false;
                try {
                    String contentDetail = fileReadProcessor.readContentDetail(customerFile);
                    success = true;
                    return contentDetail;
                } catch (Exception e) {
                    LOGGER.error("[FileReadService][error]", e);
                } finally {
                    long end = System.currentTimeMillis();
                    LOGGER.info("[FileReadService][{}][{}][{}][{}]",
                            processName,
                            end - start + "ms",
                            success,
                            fileSimpleName
                            );
                }
            }
        }

        LOGGER.error(String.format("没有找到对应的处理器[%s]", customerFile.getFileSimpleName()));

        return "";
    }
}