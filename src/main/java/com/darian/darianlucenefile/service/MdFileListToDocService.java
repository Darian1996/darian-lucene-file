package com.darian.darianlucenefile.service;

import com.darian.darianlucenefile.config.DarianGitRepConfig;
import com.darian.darianlucenefile.config.DarianLuceneConfig;


import com.darian.darianlucenefile.constants.DocumentConstants;
import com.darian.darianlucenefile.utils.AssertUtils;
import com.darian.darianlucenefile.utils.CustomerFileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a>
 * @date 2020/5/22  0:08
 */
@Service
@Slf4j
public class MdFileListToDocService implements InitializingBean {

    /**
     * 用于触发依赖的初始化，DarianGitRepConfig，优先加载
     */
    @Resource
    private DarianGitRepConfig darianGitRepConfig;

    @Resource
    private DarianLuceneConfig darianLuceneConfig;

    private static String NOT_COMPLETE_STRING_TAG = "[未完成]";

    private static boolean IS_FINISHED = false;

    private static String MD_DOC_STRING = "";

    public String getDirectoryDocMD() {
        AssertUtils.assertTrue(IS_FINISHED, "DOC_目录生成未完成");
        return MD_DOC_STRING;
    }

    public String generoterMdToDoc() {
        String docsFilePath = darianLuceneConfig.getDocsFilePath();
        Pattern pattern = darianLuceneConfig.getPattern();

        getFileFullNameList(docsFilePath, pattern);

        MD_DOC_STRING = RESULT_STRING_LIST.stream().collect(Collectors.joining("\n"));
        IS_FINISHED = true;
        return MD_DOC_STRING;
    }

    public static List<String> getFileFullNameList(String path, Pattern pattern) {
        List<String> fileStringList = new ArrayList<>();
        return getFileFullNameList(path, pattern, fileStringList, 0);
    }

    static List<String> RESULT_STRING_LIST = new ArrayList<>();

    static {
        RESULT_STRING_LIST.add("# Darian: 自动生成目录 darian-lucene-file");
    }

    /**
     * 可以跳过的文件夹的名称
     */
    public static List<String> INGORE_DIRECTORIES = Arrays.asList(".git", ".idea", "TopNtest.class", "旧",
            "assets", "photo", "viso", "图片", "LoveHistory", "胜天半子", "工程图片");

    /**
     * 文件夹，需要递归以后，根据目录的深度生成对应的目录，需要层次往后边依次增加
     *
     * @param path
     * @param deep 深度，需要拼接前边的空格
     * @return
     */
    public static List<String> getFileFullNameList(String path, Pattern pattern, List<String> fileStringList, int deep) {
        File file = new File(path);
        File[] tempList = file.listFiles();
        Arrays.sort(tempList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if ((o1.isDirectory() && o2.isDirectory()) || (o1.isFile() && o2.isFile())) {
                    return o1.getName().compareTo(o2.getName());
                } else {
                    if (o1.isDirectory()) {
                        return -1;
                    }
                    return 1;
                }
            }
        });

        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                // 完整的路径, 从盘符开始一直计算
                File itemFile = tempList[i];
                String fileFullName = tempList[i].toString();
                fileStringList.add(fileFullName);
                // 文件名，不包含路径
                String fileName = tempList[i].getName();
                if (pattern.matcher(fileName).find()) {
                    String outLineString = "- ";

                    if (isNotComplete(itemFile)) {
                        outLineString += "<font color='red'>" + fileName + "&nbsp;&nbsp;[未完成]</font>";
                    } else {
                        outLineString += fileName;
                    }

                    String itemFileFullName = itemFile.toString();
                    outLineString += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href=\""
                            + DarianGitRepConfig.getGitHubUrlFromFullName(itemFileFullName)
                            + "\"  target=\"_blank\">GitHub</a>";
                    outLineString += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href=\""
                            + DarianGitRepConfig.getGiteeUrlFromFullName(itemFileFullName)
                            + "\"  target=\"_blank\">Gitee</a>";
                    outLineString += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href=\""
                            + "/contentDetail.html?cache=true&filePathSubDocsFilePath="
                            + DarianGitRepConfig.getFilePathSubDocsPath(itemFileFullName)
                            + "\"  target=\"_blank\">MD_DOC</a>";

                    String editUrl = DarianGitRepConfig.getGiteeWebIdeEditUrlFromFullName(itemFileFullName);

                    if (itemFileFullName.endsWith("svg")) {
                        // svg 的在 webide 是预览
                        editUrl  = DarianGitRepConfig.getGiteeUrlFromFullName(itemFileFullName).replaceAll("/tree/", "/edit/");
                    }

                    outLineString += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href=\""
                            + editUrl
                            + "\"  target=\"_blank\">edit</a>";


                    for (int deepi = 0; deepi < deep; deepi++) {
                        outLineString = "  " + outLineString;
                    }

                    if (log.isDebugEnabled()) {
                        System.out.println(outLineString);
                    }
                    RESULT_STRING_LIST.add(outLineString);
                }

            }
            // 文件夹，需要递归以后，根据目录的深度生成对应的目录，需要层次往后边依次增加
            if (tempList[i].isDirectory()) {
                String directoryName = tempList[i].getName();
                // 过滤的文件夹
                if (INGORE_DIRECTORIES.contains(directoryName)) {
                    continue;
                }

                if (directoryName.contains(NOT_COMPLETE_STRING_TAG)) {
                    directoryName = "<font color='red'>" + directoryName + "</font>";

                }

                String outString = "- " + directoryName;
                for (int deepi = 0; deepi < deep; deepi++) {
                    outString = "  " + outString;
                }

                RESULT_STRING_LIST.add(outString);
                getFileFullNameList(tempList[i].toString(), pattern, fileStringList, deep + 1);
            }
        }
        return fileStringList;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        generoterMdToDoc();
    }

    /**
     * 判断文件是否已经完成了，
     * 存在 字段 "[未完成" 就是未完成
     *
     * @param itemFile
     * @return
     */
    public static boolean isNotComplete(File itemFile) {
        String fileContentString = "";
        try {
            fileContentString = CustomerFileUtils.readFileToString(itemFile, DocumentConstants.UTF_8);
        } catch (IOException e) {
            log.error("[MdToDocService.isNotComplete]读取文件读取错误:", e);
        }
        return fileContentString.contains(NOT_COMPLETE_STRING_TAG);
    }
}