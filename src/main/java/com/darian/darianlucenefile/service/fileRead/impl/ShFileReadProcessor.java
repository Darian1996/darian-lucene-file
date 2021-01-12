package com.darian.darianlucenefile.service.fileRead.impl;

import com.darian.darianlucenefile.constants.DocumentConstants;
import com.darian.darianlucenefile.domain.CustomerFile;
import org.springframework.stereotype.Service;

@Service
public class ShFileReadProcessor extends AbstractOnlyReadFileStringProcessor {

    @Override
    public boolean supportProcessor(CustomerFile customerFile) {
        return customerFile.getFileSimpleName().endsWith(".sh");
    }

    @Override
    public String getContentDetailStart() {
        return DocumentConstants.MD_CODE_BASH_START;
    }

    @Override
    public String getContentDetailEnd() {
        return DocumentConstants.MD_CODE_END;
    }
}