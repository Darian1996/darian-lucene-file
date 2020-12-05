package com.darian.darianlucenefile.service;

import com.darian.darianlucenefile.config.DocumentContants;
import com.darian.darianlucenefile.config.DarianGitRepConfig;
import com.darian.darianlucenefile.domain.CustomerFile;
import com.darian.darianlucenefile.exception.CustomerRuntimeException;
import com.darian.darianlucenefile.repository.CustomerFileRepository;
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

    /**
     * MD 文件中，图片的链接
     */
    private static String patternString = "\\!\\[.*\\]\\(.+\\)";

    private static Pattern compile = Pattern.compile(patternString);

    ConcurrentLinkedHashMap<String, CustomerFile> fileConcurrentLinkedHashMap =
            new ConcurrentLinkedHashMap.Builder<String, CustomerFile>()
                    .maximumWeightedCapacity(5)
                    .weigher(Weighers.singleton())
                    .build();

    @Resource
    private CustomerFileRepository customerFileRepository;

    @Resource
    private MdFileListToDocService mdFileListToDocService;

    public CustomerFile doGetCustomerFile(String filePathSubDocsFilePath, boolean cache) {
        AssertUtils.assertTrue(StringUtils.hasText(filePathSubDocsFilePath), "filePathSubDocsFilePath不能为空");

        return CacheFunctionUtils.getCacheOrNotCache(cache,
                () -> fileConcurrentLinkedHashMap.get(filePathSubDocsFilePath),
                () -> this.doGetCustomerFile(filePathSubDocsFilePath),
                value -> fileConcurrentLinkedHashMap.put(filePathSubDocsFilePath, value));
    }

    private CustomerFile doGetCustomerFile(String filePathSubDocsFilePath) {
        AssertUtils.assertTrue(StringUtils.hasText(filePathSubDocsFilePath), "filePathSubDocsFilePath不能为空");
        CustomerFile responseFile = customerFileRepository.findCopyCustomerFile(filePathSubDocsFilePath);
        AssertUtils.assertTrue(Objects.nonNull(responseFile), "[" + filePathSubDocsFilePath + "]文章找不到");
        responseFile.setContentDetail(doReadFileContentDetail(responseFile));
        return responseFile;
    }

    private String doReadFileContentDetail(CustomerFile responseFile) {
        /**
         * 是图片的话，填充 gitee.raw 网址，进行直接替换
         */
        if (CustomerImgUtils.isImg(responseFile.getFileSimpleName())) {
            String imgUrl = responseFile.getImageShowUrl();
            return "<a target=\"_blank\" href=\"" + imgUrl + "\">" +
                    "<img src=\"" + imgUrl + "\"/>" +
                    "</a>";
        }

        /**
         *  .md 结尾：文章的话，
         * 1. 先把 md 文件读进来
         * 2. 替换图片链接
         *
         */
        if (responseFile.getFileSimpleName().endsWith(".md")) {
            // 目录文件，更新，直接显示文件
            if ("_README_DIRECTORY_DOC.md".equals(responseFile.getFilePathSubDocsFilePath())) {
                return mdFileListToDocService.getDirectoryDocMD();
            }

            responseFile.setContentDetail(onlyReadFileString(new File(responseFile.getFileFullName())));
            initImageUrl(responseFile);


            return responseFile.getContentDetail();
        }
        if (responseFile.getFileSimpleName().endsWith(".bat")
                || responseFile.getFileSimpleName().endsWith(".java")
                || responseFile.getFileSimpleName().endsWith(".sh")) {
            return onlyReadFileString(new File(responseFile.getFileFullName()));
        }
        return null;
    }


    public static String onlyReadFileString(File itemFile) {
        String filePath = itemFile.getPath();
        try {
            StringBuffer fileContentsb = new StringBuffer();
            if (filePath.endsWith(".bat")) {
                // 保存的时候，需要转化为 ASCII 码，在CMD 运行时，才不会乱码 bat 文件用 GBK 读取，
                fileContentsb.append(DocumentContants.MD_CODE_BASH_START)
                        .append(CustomerFileUtils.readFileToString(itemFile, DocumentContants.GBK))
                        .append(DocumentContants.MD_CODE_END);
            } else if (filePath.endsWith(".md")) {
                fileContentsb.append(CustomerFileUtils.readFileToString(itemFile, DocumentContants.UTF_8));
            } else if (filePath.endsWith(".java")) {
                fileContentsb.append(DocumentContants.MD_CODE_JAVA_START)
                        .append(CustomerFileUtils.readFileToString(itemFile, DocumentContants.UTF_8))
                        .append(DocumentContants.MD_CODE_END);
            }else if (filePath.endsWith(".sh")){
                fileContentsb.append(DocumentContants.MD_CODE_BASH_START)
                        .append(CustomerFileUtils.readFileToString(itemFile, DocumentContants.UTF_8))
                        .append(DocumentContants.MD_CODE_END);
            }

            //            图片可以读取为 Base64
            /**
             *  // 去掉 Img 的读取
             *  else if (CustomerImgUtils.isImg(itemFile.getName())) {
             *  byte[] bytes = CustomerFileUtils.readFileToByteArray(itemFile);
             *  fileContent = Base64.getEncoder().encodeToString(bytes);
             *  }
             */

            else{
                fileContentsb.append("");
            }

            return fileContentsb.toString();
        } catch (IOException e) {
            log.error("内容处理发生错误：", e);
            throw new CustomerRuntimeException("内容处理发生错误：", e);
        }
    }

    /**
     * 替换 这个文件中的 imageUrl 链接为 git.raw 的 ，私人仓库需要登陆 gitee ，就可以登陆
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

            String imgCustomerFileKey = imgFullName.substring(DocumentContants.DOCS_FILE_PATH.length());

            //
            imgCustomerFileKey = DarianGitRepConfig.replaceTo_(imgCustomerFileKey);

            CustomerFile imageCustomerFile = customerFileRepository.findCopyCustomerFile(imgCustomerFileKey);

            if (imageCustomerFile == null) {
                log.error("[" + customerFile.getFileFullName() + "] find img" + "[" + imgFullName + "] not fond");
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
