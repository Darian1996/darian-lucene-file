package com.darian.darianlucenefile.service.fileRead;

import com.darian.darianlucenefile.constants.DocumentConstants;
import com.darian.darianlucenefile.domain.CustomerFile;
import com.darian.darianlucenefile.exception.CustomerRuntimeException;
import com.darian.darianlucenefile.utils.CustomerFileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public interface FileReadProcessor {

    Logger LOGGER = LoggerFactory.getLogger(FileReadProcessor.class);

    boolean supportProcessor(CustomerFile customerFile);

    String readContentDetail(CustomerFile customerFile);

    /**
     * 只读取 ，不进行 组装，替换等
     * 例：md 需要进行 img Url 的替换（这里不做）
     *
     * @param customerFile
     * @return
     */
    default String onlyReadFileString(CustomerFile customerFile) {
        File itemFile = new File(customerFile.getFileFullName());
        try {
            StringBuffer fileContentsb = new StringBuffer();

            fileContentsb.append(getContentDetailStart())
                    .append(CustomerFileUtils.readFileToString(itemFile, getReadEncode()))
                    .append(getContentDetailEnd());

            return fileContentsb.toString();
        } catch (IOException e) {
            LOGGER.error("内容处理发生错误：", e);
            throw new CustomerRuntimeException("内容处理发生错误：", e);
        }
    }

    default String getReadEncode() {
        return DocumentConstants.UTF_8;
    }

    default String getContentDetailStart() {
        return "";
    }

    default String getContentDetailEnd() {
        return "";
    }

}