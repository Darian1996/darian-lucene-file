package com.darian.darianlucenefile.config;


import com.darian.darianlucenefile.utils.AssertUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.StringUtils;


import java.io.File;
import java.util.Objects;
import java.util.regex.Pattern;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a> 
 * @date 2020/4/13  22:06
 */
@Data
@Slf4j
@Configuration
@ConfigurationProperties("darian.lucene.config")
public class DarianLuceneConfig implements InitializingBean {

    /**
     * 是否使用内存分词器, true , 使用
     */
    private Boolean ramOpen = true;

    /**
     * 是否使用 IK 分词器
     */
    private Boolean useIK = true;

    /**
     * 文章最长取多少长度
     */
    private int contentMaxSize = 800;

    /**
     * 原始文档的路径
     */
    private String docsFilePath;
//    private String docsFilePath = isWindows() ? "D:\\GitHub_Repositories\\docs" : "/software/docs";

    /**
     * 索引库创建的位置(默认的位置)
     */
    private String directoryPath = System.getProperty("user.dir")
            + File.separator + "src"
            + File.separator + "main"
            + File.separator + "resources"
            + File.separator + "luncene_directory";

    /**
     * 索引的文件
     */
    private String patternString = "\\\\*\\.bat"
            + "|\\\\*\\.md"
            + "|\\\\*\\.xmind"
            + "|\\\\*\\.jpg"
            + "|\\\\*\\.png"
            + "|\\\\*\\.JPEG"
            + "|\\\\*\\.svg"
            + "|\\\\*\\.java"
            + "|\\\\*\\.gif"
            + "|\\\\*\\.sh"
            + "|\\\\*\\.JPG";

    /**
     * 正则匹配编译后的 pattern
     */
    private Pattern pattern;

    /**
     * 结果最多取多少个
     */
    private int resutMaxSize = 30;

    private String redHighLightBegin = "<b><font color='red'>";

    private String redHighLightEnd = "</font></b>";

    @Override
    public void afterPropertiesSet() throws Exception {
        AssertUtils.assertTrue(ramOpen != null,
                "darian.lucene.config.ramOpen must not be null");
        AssertUtils.assertTrue(useIK != null,
                "darian.lucene.config.useIK must not be null");
        AssertUtils.assertTrue(contentMaxSize != 0,
                "darian.lucene.config.contentMaxSize must not be 0");
        AssertUtils.assertTrue(StringUtils.hasText(docsFilePath),
                "darian.lucene.config.docsFilePath must not blank");
        if (docsFilePath.endsWith("/")) {
            docsFilePath = docsFilePath.substring(0, docsFilePath.length() -1);
        }
        AssertUtils.assertTrue(StringUtils.hasText(patternString),
                "darian.lucene.config.patternString must not blank");

        pattern = Pattern.compile(patternString);
        AssertUtils.assertTrue(Objects.nonNull(pattern), "darian.lucene.config.pattern must not null");

        DocumentContants.DOCS_FILE_PATH = docsFilePath;

    }

    public boolean isWindows() {
        return System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1;
    }

    @Override
    public String toString() {
        return "\n" + "[DarianLuceneConfig]" + "\n" +
                "{" + "\n"+
                "\t" + "ramOpen = \"" + ramOpen + "\",\n " +
                "\t" + "useIK = \"" + useIK + "\",\n " +
                "\t" + "contentMaxSize = \"" + contentMaxSize + "\",\n " +
                "\t" + "docsFilePath = \"" + docsFilePath + "\",\n " +
                "\t" + "directoryPath = \"" + directoryPath + "\",\n " +
                "\t" + "patternString = \"" + patternString + "\",\n " +
                "\t" + "pattern = \"" + pattern + "\",\n " +
                "\t" + "resutMaxSize = \"" + resutMaxSize + "\",\n " +
                "\t" + "redHighLightBegin = \"" + redHighLightBegin + "\",\n " +
                "\t" + "redHighLightEnd = \"" + redHighLightEnd + "\",\n " +
                '}';
    }
}
