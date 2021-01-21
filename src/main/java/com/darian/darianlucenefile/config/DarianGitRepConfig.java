package com.darian.darianlucenefile.config;

import com.darian.darianlucenefile.utils.AssertUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.File;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a> 
 * @date 2020/4/13  2:34
 */
@Data
@Slf4j
@Configuration
@ConfigurationProperties("darian.mygit.config")
@AutoConfigureAfter(DarianLuceneConfig.class)
public class DarianGitRepConfig implements InitializingBean {

    @Resource
    private DarianLuceneConfig darianLuceneConfig;

    /**
     * Git 个人仓库对应的路径，（authorName）
     */
    private String gitAuthorName = "Darian1996";

    /**
     * githubAuthorName
     */
    private String githubAuthorName = null;

    /**
     * giteeAuthorName
     */
    private String giteeAuthorName = null;

    private String gitBranch = "master";

    /**
     * git 根路径，必存在 .git 文件
     */
    private static String gitPathHasFileName = ".git";

    public static String GITHUB_URL_PRE = null;

    public static String GITEE_URL_PRE = null;

    public static String GITEE_WEB_IDE_EDIT_URL_PRE = null;

    private static String DOCS_FILE_PATH = null;


    @Override
    public void afterPropertiesSet() throws Exception {
        if (!StringUtils.hasText(gitAuthorName)) {
            log.warn("darian.mygit.config.gitAuthorName is blank");
        }
        AssertUtils.assertTrue(StringUtils.hasText(gitAuthorName),
                "[darian.mygit.config.gitAuthorName] must not be Blank");

        githubAuthorName = StringUtils.hasText(githubAuthorName) ? githubAuthorName : gitAuthorName;
        giteeAuthorName = StringUtils.hasText(giteeAuthorName) ? giteeAuthorName : gitAuthorName;

        AssertUtils.assertTrue(StringUtils.hasText(githubAuthorName),
                "[darian.mygit.config.githubAuthorName] must not be Blank");
        AssertUtils.assertTrue(StringUtils.hasText(giteeAuthorName),
                "[darian.mygit.config.giteeAuthorName] must not be Blank");
        AssertUtils.assertTrue(StringUtils.hasText(gitBranch),
                "[darian.mygit.config.gitBranch] must not be Blank");
        AssertUtils.assertTrue(StringUtils.hasText(gitPathHasFileName),
                "[darian.mygit.config.gitPathHasFileName] must not be Blank");

        String docsFilePath = darianLuceneConfig.getDocsFilePath();
        GITHUB_URL_PRE = initGitHubUrlPre(docsFilePath);
        GITEE_URL_PRE = initGiteeUrlPre(docsFilePath);
        GITEE_WEB_IDE_EDIT_URL_PRE = initGiteeWebIdeEditUrlPre(docsFilePath);

        DOCS_FILE_PATH = docsFilePath;

        AssertUtils.assertTrue(StringUtils.hasText(GITHUB_URL_PRE),
                "[darian.mygit.config.GITHUB_URL_PRE] must not be Blank");
        AssertUtils.assertTrue(StringUtils.hasText(GITEE_URL_PRE),
                "[darian.mygit.config.GITEE_URL_PRE] must not be Blank");


    }

    public static String getGitHubUrlFromFullName(String fileFullName) {
        String pathPreAndFile = fileFullName.substring(new File(DOCS_FILE_PATH).toString().length())
                .replaceAll("\\\\", "/").replaceAll(" ", "%20");

        if (StringUtils.hasText(GITHUB_URL_PRE)) {
            return GITHUB_URL_PRE + pathPreAndFile;
        }
        return "";
    }

    /**
     * 文件全路径 - 仓库路径作为文件的唯一Id
     * / 和 \ 全部转化为 '_'
     *
     * @param fileFullName
     * @return
     */
    public static String getFilePathSubDocsPath(String fileFullName) {
        return replaceTo_(fileFullName.substring(new File(DOCS_FILE_PATH).toString().length()));
    }

    public static String replaceTo_(String str) {
        return str.replaceAll("\\\\", "_")
                .replaceAll("/", "_")
                .replaceAll("\\+", "_")
                .replaceAll("&", "_")
                .replaceAll("\\[", "_")
                .replaceAll("]", "_")
                .replaceAll("\\{", "_")
                .replaceAll("}", "_")
                .replaceAll("<", "_")
                .replaceAll(">", "_")
                .replaceAll("＜", "_")
                .replaceAll("＞", "_")
                .replaceAll("「", "_")
                .replaceAll("」", "_")
                .replaceAll("：", "_")
                .replaceAll("；", "_")
                .replaceAll("、", "_")
                .replaceAll("•", "_")
                .replaceAll("\\^", "_")
                .replaceAll("'", "_")
                .replaceAll(" ", "_")
                .replaceAll("-", "_")
                ;
    }


    public static String getGiteeUrlFromFullName(String fileFullName) {
        String pathPreAndFile = fileFullName.substring(new File(DOCS_FILE_PATH).toString().length())
                .replaceAll("\\\\", "/").replaceAll(" ", "%20");

        if (StringUtils.hasText(GITEE_URL_PRE)) {
            return GITEE_URL_PRE + pathPreAndFile;
        }
        return "";
    }


    public static String getGiteeWebIdeEditUrlFromFullName(String fileFullName) {
        String pathPreAndFile = fileFullName.substring(new File(DOCS_FILE_PATH).toString().length())
                .replaceAll("\\\\", "/").replaceAll(" ", "%20");

        if (StringUtils.hasText(GITEE_WEB_IDE_EDIT_URL_PRE)) {
            return GITEE_WEB_IDE_EDIT_URL_PRE + pathPreAndFile;
        }
        return "";
    }

    private String initGiteeWebIdeEditUrlPre(String filePath) {
        String findGitRepositoryName = findGitRepositoryName(filePath);

        AssertUtils.assertTrue(StringUtils.hasText(findGitRepositoryName), "[" + filePath + "]下边没有找到Git仓库");

        String git_file_pre = filePath.substring(
                filePath.indexOf(File.separator + findGitRepositoryName) + 1 + findGitRepositoryName.length())
                .replaceAll("\\\\", "/");

        String github_repository_Url = "https://gitee.com/-/ide/project/" + githubAuthorName + "/" + findGitRepositoryName + "/edit/"+gitBranch+"/-";

        return (github_repository_Url + git_file_pre)
                .replaceAll("\\\\", "/")
                .replaceAll(" ", "%20");
    }

    /**
     * 初始化仓库的前缀，整个仓库的前缀(GitHub)
     *
     * @param filePath
     * @return
     */
    private String initGitHubUrlPre(String filePath) {
        String findGitRepositoryName = findGitRepositoryName(filePath);

        AssertUtils.assertTrue(StringUtils.hasText(findGitRepositoryName), "[" + filePath + "]下边没有找到Git仓库");

        String git_file_pre = filePath.substring(
                filePath.indexOf(File.separator + findGitRepositoryName) + 1 + findGitRepositoryName.length())
                .replaceAll("\\\\", "/");

        String github_repository_Url = "https://github.com/" + githubAuthorName + "/" + findGitRepositoryName + "/tree/" + gitBranch;
        return (github_repository_Url + git_file_pre)
                .replaceAll("\\\\", "/")
                .replaceAll(" ", "%20");
    }

    /**
     * 初始化仓库的前缀，整个仓库的前缀(Gitee)
     *
     * @param filePath
     * @return
     */
    private String initGiteeUrlPre(String filePath) {
        String findGitRepositoryName = findGitRepositoryName(filePath);
        AssertUtils.assertTrue(StringUtils.hasText(findGitRepositoryName), "[" + filePath + "]下边没有找到Git仓库");

        String git_file_pre = filePath.substring(
                filePath.indexOf(File.separator + findGitRepositoryName) + 1 + findGitRepositoryName.length())
                .replaceAll("\\\\", "/");

        String gitee_repository_Url = "https://gitee.com/" + giteeAuthorName + "/" + findGitRepositoryName + "/tree/" + gitBranch;

        return (gitee_repository_Url + git_file_pre)
                .replaceAll("\\\\", "/")
                .replaceAll(" ", "%20");

    }

    private String findGitRepositoryName(String filePath) {
        log.debug("寻找 Git 仓库[filePath]： " + filePath);
        String findGitRepositoryName = null;

        // windows 的 结束和 linux 的结束
        if (filePath.endsWith(":\\") || "/".equals(filePath)) {
            log.error("已经找到了盘符：" + filePath);
            return null;
        }
        File file = new File(filePath);
        AssertUtils.assertTrue(file.exists(), "[" + filePath + "]git 不仓库存在!!!");
        AssertUtils.assertTrue(file.listFiles().length > 0, "Git[" + filePath + "] 仓库下的文件 > 0");
        for (File fileItem : file.listFiles()) {
            if (fileItem.isDirectory()) {
                if (fileItem.getName().equals(gitPathHasFileName)) {
                    findGitRepositoryName = file.getName();
                    break;
                }
            }
        }
        if (findGitRepositoryName == null) {
            return findGitRepositoryName(file.getParent());
        }
        return findGitRepositoryName;
    }

    @Override
    public String toString() {
        return "\n" + "[DarianGitRepConfig]" +
                "\n" + "{" + "\n" +
                "\t" + "gitAuthorName = \"" + gitAuthorName + "\",\n " +
                "\t" + "githubAuthorName = \"" + githubAuthorName + "\",\n " +
                "\t" + "giteeAuthorName = \"" + giteeAuthorName + "\",\n " +
                "\t" + "gitBranch = \"" + gitBranch + "\",\n " +
                "\t" + "githubAuthorName = \"" + githubAuthorName + "\",\n " +
                "\t" + "GITHUB_URL_PRE = \"" + GITHUB_URL_PRE + "\",\n " +
                "\t" + "GITEE_URL_PRE = \"" + GITEE_URL_PRE + "\"\n " +
                '}';
    }
}
