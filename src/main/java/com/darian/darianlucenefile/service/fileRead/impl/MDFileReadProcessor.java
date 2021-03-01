package com.darian.darianlucenefile.service.fileRead.impl;

import com.darian.darianlucenefile.config.DarianGitRepConfig;
import com.darian.darianlucenefile.constants.DocumentConstants;
import com.darian.darianlucenefile.domain.CustomerFile;
import com.darian.darianlucenefile.repository.CustomerFileRepository;
import com.darian.darianlucenefile.service.MdFileListToDocService;
import com.darian.darianlucenefile.service.fileRead.FileReadProcessor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MDFileReadProcessor implements FileReadProcessor {

    /**
     * MD 文件中，图片的链接
     */
    private static String patternString = "\\!\\[.*\\]\\(.+\\)";

    private static Pattern compile = Pattern.compile(patternString);

    @Resource
    private MdFileListToDocService mdFileListToDocService;

    @Resource
    private CustomerFileRepository customerFileRepository;

    @Override
    public boolean supportProcessor(CustomerFile customerFile) {
        return customerFile.getFileSimpleName().endsWith(".md");
    }

    @Override
    public String readContentDetail(CustomerFile customerFile) {
        // 目录文件，更新，直接显示文件
        if ("_README_DIRECTORY_DOC.md".equals(customerFile.getFilePathSubDocsFilePath())) {
            return mdFileListToDocService.getDirectoryDocMD();
        }
        if ("_README_PUML_SVG_DOC.md".equals(customerFile.getFilePathSubDocsFilePath())) {
            return mdFileListToDocService.getPumlAndSVGMD();
        }

        customerFile.setContentDetail(onlyReadFileString(customerFile));

        initImageUrl(customerFile);

        return customerFile.getContentDetail();
    }

    /**
     * 替换 这个文件中的 imageUrl 链接为 git.raw 的 ，私人仓库需要登陆 gitee ，就可以登陆
     *
     * <p>步骤：</p>
     *
     * <p>1: 匹配图片的正则</p>
     * <p>2：替换为图片的路径</p>
     * <p>3：替换为图片的文件路径</p>
     * <p>4：去内存文件仓库中查询图片文件信息</p>
     * <p>5：替换图片为 内存图片信息的 展示URL </p>
     *
     * @param customerFile
     */
    public void initImageUrl(CustomerFile customerFile) {
        String contentDetail = customerFile.getContentDetail();
        String fileFullName = customerFile.getFileFullName();
        fileFullName.substring(0, fileFullName.length() - customerFile.getFileSimpleName().length());

        String imagePathPre = fileFullName.substring(0, fileFullName.lastIndexOf(File.separator) + 1);

        Matcher matcher = compile.matcher(customerFile.getContentDetail());

        while (matcher.find()) {
            String assetsImgPre = matcher.group(0);
            String escapeAssetsImgPre = assetsImgPre.replace("/", File.separator);
            escapeAssetsImgPre = escapeAssetsImgPre.substring(escapeAssetsImgPre.indexOf("(") + 1, escapeAssetsImgPre.indexOf(")"));

            String imgFullName = imagePathPre + escapeAssetsImgPre;

            String imgCustomerFileKey = imgFullName.substring(DocumentConstants.DOCS_FILE_PATH.length());

            //
            imgCustomerFileKey = DarianGitRepConfig.replaceTo_(imgCustomerFileKey);

            CustomerFile imageCustomerFile = customerFileRepository.findCopyCustomerFile(imgCustomerFileKey);

            if (imageCustomerFile == null) {
                LOGGER.error(String.format("[%s] find img:[%s] not fond",
                        customerFile.getFileFullName(),
                        imgFullName));
            } else {
                String imageShowUrl = imageCustomerFile.getImageShowUrl();

                String imgUrlString = "<a target=\"_blank\" href=\" " + imageShowUrl + "\">" +
                        "<img src=\" " + imageShowUrl + "\"/>" +
                        "</a>";
                contentDetail = contentDetail.replace(assetsImgPre, imgUrlString);

            }
        }
        // 替换完毕，放回去
        customerFile.setContentDetail(contentDetail);
    }
}