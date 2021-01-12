package com.darian.darianlucenefile.service.fileRead.impl;

import com.darian.darianlucenefile.constants.DocumentConstants;
import com.darian.darianlucenefile.domain.CustomerFile;
import org.springframework.stereotype.Service;

@Service
public class PlantUmlFileReadProcessor extends AbstractOnlyReadFileStringProcessor {

    @Override
    public boolean supportProcessor(CustomerFile customerFile) {
        return customerFile.getFileSimpleName().endsWith(".puml");
    }

    @Override
    public String getContentDetailStart() {
        return DocumentConstants.MD_CODE_PLANTUML_START;
    }

    @Override
    public String getContentDetailEnd() {
        return DocumentConstants.MD_CODE_END;
    }
}