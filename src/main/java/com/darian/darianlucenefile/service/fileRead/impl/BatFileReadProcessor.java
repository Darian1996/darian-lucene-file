package com.darian.darianlucenefile.service.fileRead.impl;

import com.darian.darianlucenefile.constants.DocumentConstants;
import com.darian.darianlucenefile.domain.CustomerFile;
import org.springframework.stereotype.Service;

@Service
public class BatFileReadProcessor extends AbstractOnlyReadFileStringProcessor {

    @Override
    public boolean supportProcessor(CustomerFile customerFile) {
        return customerFile.getFileSimpleName().endsWith(".bat");
    }

    @Override
    public String getContentDetailStart() {
        return DocumentConstants.MD_CODE_BAT_START;
    }

    @Override
    public String getContentDetailEnd() {
        return DocumentConstants.MD_CODE_END;
    }

    @Override
    public String getReadEncode() {
        // 保存的时候，需要转化为 ASCII 码，在CMD 运行时，才不会乱码 bat 文件用 GBK 读取，
        return DocumentConstants.GBK;
    }
}