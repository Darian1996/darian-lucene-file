package com.darian.darianlucenefile.utils;


import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a> 
 * @date 2020/4/12  20:49
 */
@Slf4j
public class ShellUtils {

    /**
     * 白名单 IP 需要暴漏的端口
     * 2181 zookeeper
     * 6379 redis
     * 8848 nacos
     * 9092 kafkas
     * 3306 mysql
     */
    //    static Integer[] PORT_S = new Integer[]{2181, 6379, 8848, 9092, 3306};

    public static List<Integer> PORT_S = new ArrayList<>();

    /**
     * 查看完整的 commitId:  git log --pretty=format:"%H" | head -1  | awk '{print $1}'
     * 查看简短的 commitId:  git log --pretty=format:"%h" | head -1  | awk '{print $1}'
     *
     * @return
     */
    public static String getDocsCommitId() {
        runShString("cd /software/docs \n git pull gitee master");
        String getLastCommitId = "cd /software/docs \n git log --pretty=format:\"%H\" | head -1  | awk '{print $1}'";
        return runShString(getLastCommitId);
    }

    public static String getDarianLuceneCommitId() {
        runShString("cd /software/darian-lucene-file \n git pull gitee master");
        String getLastCommitId = "cd /software/darian-lucene-file \n git log --pretty=format:\"%H\" | head -1  | awk '{print $1}'";
        return runShString(getLastCommitId);
    }

    public static String getDarianLuceneVertxCommitId() {
        runShString("cd /software/darian-lucene-vertx \n git pull gitee master");
        String getLastCommitId = "cd /software/darian-lucene-vertx \n git log --pretty=format:'%H' | head -1  | awk '{print $1}'";
        return runShString(getLastCommitId);
    }

    /**
     * 获取所有的 IP 的白名单
     *
     * @return
     */
    public static Set<String> getAllWhiteIPSet() {
        String allLine = firewall_cmd_list_all();

        allLine = allLine == null ? "" : allLine;

        String[] split = allLine.split("\n");
        Set<String> iPList = new HashSet<>();
        for (String str : split) {
            if (str.contains("source address=")) {
                String substring = str.substring(str.indexOf("source address=") + "source address=\"".length());
                substring = substring.substring(0, substring.indexOf("\""));

                iPList.add(substring);
            }
        }

        return iPList;
    }


    /**
     * 写一个脚本，可以快速的重启应用
     *
     * @throws Exception
     */
    public static void thisAppplicationReStartSh() {
        String docs_pull_bash = "sh /software/sh/darian-lucene-restart.sh";
        runShString(docs_pull_bash);
    }

    /**
     * 获取所有的 防火墙配置信息
     *
     * @return
     */
    public static String firewall_cmd_list_all() {
        String firewall_cmd_list_all_bash = "firewall-cmd --list-all";
        return runShString(firewall_cmd_list_all_bash);
    }

    /**
     * 给一些端口，添加 IP 白名单
     *
     * @param whiteIp
     * @return
     */
    public static String firewall_bash_add_ip_white(String whiteIp) {

        String result = "";
        for (Integer port : PORT_S) {
            String one_bash_String =
                    "firewall-cmd " +
                            "--permanent " +
                            "--add-rich-rule='rule " +
                            "family='ipv4' " +
                            "source address='" + whiteIp + "' " +
                            "port protocol='tcp' " +
                            "port='" + port + "' " +
                            "accept'";

            result += runShString(one_bash_String);
        }

        result += runShString("firewall-cmd --reload");

        return result;
    }

    /**
     * 把白名单里的 IP 进行删除
     *
     * @param whiteIp
     * @return
     */
    public static String firewall_bash_delete_ip_white(String whiteIp) {
        String result = "";
        for (Integer port : PORT_S) {
            String one_bash_String =
                    "firewall-cmd " +
                            "--permanent " +
                            "--remove-rich-rule='rule " +
                            "family='ipv4' " +
                            "source " +
                            "address='" + whiteIp + "' " +
                            "port protocol='tcp' " +
                            "port='" + port + "' " +
                            "accept'";

            result += runShString(one_bash_String);
        }

        result += runShString("firewall-cmd --reload");
        return result;
    }

    /**
     * 运行 sh 命令的
     *
     * @param shString
     * @return
     */
    private static String runShString(String shString) {
        if (System.getProperty("os.name").contains("Windows")) {
            log.debug("系统是 windows 系统，不执行 sh 命令 ... ... ");
            return "系统是 windows 系统，不执行 sh 命令 ... ... ";
        }
        try {
            Process ps = null;
            if (System.getProperty("os.name").contains("Windows")) {
                // windows 系统处理
                ps = Runtime.getRuntime().exec(shString);
            } else {
                // Linux 系统处理
                // 这个可以处理 sh 命令中的 双引号 单引号的问题
                ps = Runtime.getRuntime().exec(new String[]{"sh", "-c", shString});
            }

            ps.waitFor();
            try (
                    InputStream inputStream = ps.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader)
            ) {
                StringBuffer sb = new StringBuffer();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line)
                            .append("\n");
                }
                String result = sb.toString();
                log.debug("sh: \n" + shString + "\n" + "result:\n" + result);
                return result;
            }


        } catch (Exception e) {
            log.error("sh 命令执行报错！！！", e);
        }
        return "";
    }
}
