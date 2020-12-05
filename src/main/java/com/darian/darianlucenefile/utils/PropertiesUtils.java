package com.darian.darianlucenefile.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a> 
 * @date 2020/5/22  1:52
 */
public class PropertiesUtils {
    private PropertiesUtils(){
    }
    public static Properties load(String path) {
        Properties properties = new Properties();
        try {
            InputStream resourceAsStream = PropertiesUtils.class.getResourceAsStream(path);
            properties.load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
