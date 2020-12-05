package com.darian.darianlucenefile.service;

import com.darian.darianlucenefile.utils.ShellUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a> 
 * @date 2020/6/10  19:53
 */
@Service
@Slf4j
public class IPPortsWhiteService {

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
                    resultString += ShellUtils.firewall_bash_delete_ip_white(deleteIP);
                    resultString += "\n";
                }
            }
        }

        /**
         * 把这个 IP 添加到 所有的端口里边
         */
        resultString += ShellUtils.firewall_bash_add_ip_white(whiteIP);
        resultString += "\n";

        /**
         * 查询当前的所有信息
         */
        resultString += ShellUtils.firewall_cmd_list_all();
        return resultString;
    }
}
