package com.darian.darianlucenefile.other;


import com.darian.darianlucenefile.utils.AssertUtils;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.regex.Pattern;


/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a>
 * @date 2020/4/13  1:42
 */
@SpringBootTest
public class LuceneCustomerUtilsTest {

    @Test
    public void test() {
    }

    @Test
    public void testsubEndIncompleteLabels() {

        String patternString = "\\\\*\\.bat"
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

        Pattern pattern = Pattern.compile(patternString);

        AssertUtils.assertTrue(pattern.matcher(".md").find(), "断言");
        AssertUtils.assertFalse(pattern.matcher("md").find(), "断言");
        AssertUtils.assertTrue(pattern.matcher(".xmind").find(), "断言");
        AssertUtils.assertFalse(pattern.matcher("xmind").find(), "断言");

        System.out.println("success");

        String fileContent = "\n"
                + "> [Docker Enterprise 介绍文档](https://docs.docker.com/ee/supported-platforms/) \n"
                + ">\n"
                + "> Docker Enterprise is designed for enterprise development as well as IT teams who build, share, and run "
                + "business-critical applications at scale in production. \n"
                + ">\n"
                + "> Docker Enterprise专为企业开发以及IT团队而设计，这些IT团队在生产中大规模构建，共享和运行关键业务应用程序。";

        System.out.println(fileContent.replaceAll("\\s*", ""));
    }

}