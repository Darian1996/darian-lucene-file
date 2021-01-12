package com.darian.darianlucenefile.service.fileRead.impl;

import com.darian.darianlucenefile.domain.CustomerFile;
import com.darian.darianlucenefile.service.fileRead.FileReadProcessor;
import org.springframework.stereotype.Service;

@Service
public class ImgFileReadProcessor implements FileReadProcessor {

    @Override
    public boolean supportProcessor(CustomerFile customerFile) {
        String fileName = customerFile.getFileSimpleName();
        return fileName.endsWith(".jpg")
                || fileName.endsWith(".JPG")
                || fileName.endsWith(".png")
                || fileName.endsWith(".svg")
                || fileName.endsWith(".JPEG")
                || fileName.endsWith(".gif");
    }

    @Override
    public String readContentDetail(CustomerFile customerFile) {
        String imgUrl = customerFile.getImageShowUrl();

        return "<a target=\"_blank\" href=\"" + imgUrl + "\">" +
                "<img src=\"" + imgUrl + "\"/>" +
                "</a>";
    }

    //            图片可以读取为 Base64
    /**
     *  // 去掉 Img 的读取
     *  else if (CustomerImgUtils.isImg(itemFile.getName())) {
     *  byte[] bytes = CustomerFileUtils.readFileToByteArray(itemFile);
     *  fileContent = Base64.getEncoder().encodeToString(bytes);
     *  }
     */
}