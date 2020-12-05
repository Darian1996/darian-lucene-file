package com.darian.darianlucenefile.other;

import com.darian.darianlucenefile.utils.CustomerFileUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * md 文件处理成纯文本
 */
@Slf4j
public class RegularExpressionsUtils {

    public static String doInsertContextIndexPre(File itemFile, String encoding) {
        String fileContent = "";
        try {
            List<String> lineList = CustomerFileUtils.readLines(itemFile, encoding);

            fileContent = processMDString(lineList);
        } catch (Exception e) {
            log.error("预处理失败--", e);
        }
        return fileContent;
    }

    private static String processFirst(List<String> stringList) {
        String str = "";
        for (String stringItem : stringList) {
            char[] chars = stringItem.toCharArray();
            boolean addWhite = false;
            while (chars.length > 0 && (chars[0] == ' ' || chars[0] == '>' || chars[0] == '-')) {
                stringItem = stringItem.substring(1);
                chars = stringItem.toCharArray();
                addWhite = true;
            }
            // 去掉，由于首字母，不连贯，而造成的，拼接上一个空格
            if (addWhite) {
                stringItem = " " + stringItem;
            }
            str = str + " " + stringItem;
        }
        return str;
    }

    /**
     * 处理 .md 的纯文本
     *
     * @param stringList
     * @return
     */
    private static String processMDString(List<String> stringList) {
        List<String> collect = stringList.stream()
                .map(itemString -> {
                    // "```java "  "```jsp"   等等都需要移除掉
                    if (itemString.startsWith("```")) {
                        itemString = "";
                    }
                    // 去掉表格 '| ----------' '- '  ' -'
                    itemString = itemString.replaceAll("\\| -+", " ");
                    itemString = itemString.replaceAll("\\| ", " ");
                    itemString = itemString.replaceAll(" \\|", " ");

                    // 去掉无序列表
                    itemString = itemString.replaceAll("- ", " ");
                    // 去掉有序列表
                    return itemString.replaceAll("[*0-9]\\d*\\. ", " ");
                }).collect(Collectors.toList());

        // 一行的开头的 空格 ' ' 和 '>' '-' 都去掉,
        String string = processFirst(collect);

        // 去掉 md 文件中的 图片  (2020-05-15，修改)
        string = string.replaceAll("\\\\!\\[.*\\\\]\\(.+\\)", " ");
        // 去掉超链接，并保存下来文字
        string = string.replaceAll("\\[|\\]\\(.*?\\)", " ");
        // 去掉 md 文件中的 `
        string = string.replaceAll("`", " ");
        // 去掉 md 文件中的 "#"
        string = string.replaceAll("#", " ");
        // 去掉 md 文件中的 "*"
        string = string.replaceAll("\\*", " ");

        String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式
        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(string);
        string = m_html.replaceAll(" "); //过滤html标签
        string = string.replaceAll("&nbsp;", " ");

        /**
         // 去掉空格的话, 多个英文字母会重叠在一起，影响匹配，所以该分隔开的还是分隔开，提高分词准确率，多个空格和换行，替换为单个空格
         // string = string.replaceAll("\\s*", "");
         */
        string = string.replaceAll("\\s+", " ");

        return string;
    }

    /**
     * \s 匹配空格
     * \s* 匹配多个空格
     *
     * "\\[|\\]\\(.*\\)" 可以匹配到 [任意字符](dafadsg)，提取出来任意字符
     */

    /**
     * 提取 [xxx](bbb) 中的文本xxx
     *
     * @param string
     * @return
     */
    public static String replaceUrlPattern(String string) {
        String str = "sdfa<html>user</html>sdfasd";
        String regex = "<html>|</html>";
        String newStr = "";
        String str2 = str.replaceAll(regex, newStr);
        System.out.println(str2);
        System.out.println();

        str = "dsfa=[-dsa-](-dsafadsgad-)=sdf(dasf)";
        regex = "\\[|\\]\\(.*\\)";
        return str.replaceAll("\\[|\\]\\(.*\\)", "");
    }

    public static void main(String[] args) {
        System.out.println(RegularExpressionsUtils.replaceUrlPattern("xx"));
    }
}