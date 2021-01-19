package com.darian.darianlucenefile.service;

import com.darian.darianlucenefile.utils.ShellUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a> 
 * @date 2020/6/10  19:53
 */
@Service
@Slf4j
public class IPPortsWhiteService {

    public static List<Integer> PORT_S = new ArrayList<>();

    /**
     * 传进来想要添加的 IP
     * <p>
     * 1. 获取目前所有的 IP 白名单
     * 2. 判断是否需要删除一些 IP
     * 2.1 (allWhiteIPSet.size() <= 1) 不需要处理
     * 2.2 (allWhiteIPSet.size() == 2 && allWhiteIPSet.contains(whiteIP)) 不需要处理
     * 2.3 剩下的，把所有的 IP 都清空，再把当前的 IP 添加到白名单里边
     *
     * @param whiteIP 要添加的 IP 名单
     * @return 返回执行的结果
     */
    public String refreshWhiteIpList(String whiteIP) {
        String resultString = "";

        Set<String> allWhiteIPSet = ShellUtils.getAllWhiteIPSet();

        ArrayList<String> shStringList = new ArrayList<>();

        if (allWhiteIPSet != null) {
            if (allWhiteIPSet.size() <= 1) {
                log.debug("(allWhiteIPSet.size() <= 1) 不需要处理");
            } else if (allWhiteIPSet.size() == 2 && allWhiteIPSet.contains(whiteIP)) {
                log.debug("(allWhiteIPSet.size() == 2 && allWhiteIPSet.contains(whiteIP)) 不需要处理");
            } else {
                /**
                 * 清空已经存在在白名单里边的 IP
                 */
                for (String deleteIP : allWhiteIPSet) {
                    for (Integer port : PORT_S) {
                        String one_bash_String =
                                "firewall-cmd " +
                                        "--permanent " +
                                        "--remove-rich-rule='rule " +
                                        "family='ipv4' " +
                                        "source " +
                                        "address='" + deleteIP + "' " +
                                        "port protocol='tcp' " +
                                        "port='" + port + "' " +
                                        "accept'";

                        shStringList.add(one_bash_String);
                    }
                    //resultString += ShellUtils.firewall_bash_delete_ip_white(deleteIP);
                    //resultString += "\n";
                }
            }
        }

        /**
         * 把这个 IP 添加到 所有的端口里边
         */
        for (Integer port : PORT_S) {
            String one_bash_String =
                    "firewall-cmd " +
                            "--permanent " +
                            "--add-rich-rule='rule " +
                            "family='ipv4' " +
                            "source address='" + whiteIP + "' " +
                            "port protocol='tcp' " +
                            "port='" + port + "' " +
                            "accept'";

            shStringList.add(one_bash_String);
        }

        //resultString += ShellUtils.firewall_bash_add_ip_white(whiteIP);
        //resultString += "\n";

        shStringList.add("firewall-cmd --reload");
        /**
         * 查询当前的所有信息
         */
        shStringList.add("firewall-cmd --list-all");

        String shString = shStringList.stream().collect(Collectors.joining("\n"));
        log.info("\n" + shString);


        //resultString += ShellUtils.firewall_cmd_list_all();
        return ShellUtils.runShString(shString);
    }

}
