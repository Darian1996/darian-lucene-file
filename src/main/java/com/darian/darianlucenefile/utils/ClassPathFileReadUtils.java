package com.darian.darianlucenefile.utils;

import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a> 
 * @date 2020/9/2  21:47
 */
public class ClassPathFileReadUtils {

    public static String readClassPathResource(String filepath) {
        try {
            InputStream inputStream = new ClassPathResource(filepath).getInputStream();

            return new BufferedReader(
                    new InputStreamReader(inputStream, "UTF-8"))
                    .lines()
                    .collect(Collectors.joining(System.lineSeparator()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
