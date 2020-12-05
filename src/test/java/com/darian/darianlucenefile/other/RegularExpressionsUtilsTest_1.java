package com.darian.darianlucenefile.other;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a> 
 * @date 2020/5/10  14:31
 */
public class RegularExpressionsUtilsTest_1 {

    @Test
    public void test() {
        System.out.println("sdfa dsoaifo    sojafiasdf \n dsafasdfa"
                .replaceAll("\\s+", " "));
    }

    @Test
    public void test2() {
        String htmlStr = " Darian: 自动生成目录20172017-01-工程化专题Maven-James.md     <a href=\"https://github"
                + ".com/Darian1996/docs/tree/master/2017/2017-01-工程化专题/Maven-James.md\" target=\"_blank\">GitHub</a>     <a "
                + "href=\"https://gitee.com/Darian1996/docs/tree/master/2017/2017-01-工程化专题/Maven-James.md\" target=\"_blank\">Gitee</a>  "
                + "   <a href=\"https://www.darian.top/contentDetail"
                + ".html?cache=true&filePathSubDocsFilePath=_2017_2017-01-工程化专题_Maven-James.md\" "
                + "target=\"_blank\">top_doc</a>2017-05-01-什么是性能优化-james什么是性能优化.md     <a href=\"https://github"
                + ".com/Darian1996/docs/tree/master/2017/2017-05-01-什么是性能优化-james/什么是性能优化.md\" target=\"_blank\">GitHub</a>     <a "
                + "href=\"https://gitee.com/Darian1996/docs/tree/master/2017/2017-";
        String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式
        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); //过滤html标签

        System.out.println(htmlStr);
    }
}
