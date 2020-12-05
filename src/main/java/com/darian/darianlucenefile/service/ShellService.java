package com.darian.darianlucenefile.service;

import com.darian.darianlucenefile.utils.ShellUtils;
import org.springframework.stereotype.Service;


/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a> 
 * @date 2020/4/18  0:06
 */
@Service
public class ShellService {

    public void doRestart() {
        ShellUtils.thisAppplicationReStartSh();
    }

}