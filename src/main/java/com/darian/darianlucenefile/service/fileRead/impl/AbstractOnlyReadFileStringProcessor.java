package com.darian.darianlucenefile.service.fileRead.impl;

import com.darian.darianlucenefile.domain.CustomerFile;
import com.darian.darianlucenefile.service.fileRead.FileReadProcessor;

public abstract class AbstractOnlyReadFileStringProcessor implements FileReadProcessor {

    @Override
    public final String readContentDetail(CustomerFile customerFile) {
        return onlyReadFileString(customerFile);
    }
}