package com.darian.darianlucenefile.exception;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a> 
 * @date 2020/4/13  1:11
 */
public class AssertionException extends RuntimeException {
    public static String GIT_REPOSITORY_NAME_NOT_FOUND = "没有找到Git仓库";
    public static String PATH_MUST_BE_DIRECTORY = "path 必须是文件夹！";

    public AssertionException(String msg) {
        super(msg);
    }
}
