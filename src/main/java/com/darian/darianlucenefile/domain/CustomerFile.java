package com.darian.darianlucenefile.domain;

import lombok.Data;

import java.util.Objects;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a> 
 * @date 2020/4/12  21:39
 */
@Data
public class CustomerFile {
    // 文件姓名
    private String fileSimpleName;
    // 文件内容
    private String contentDetail;
    // 文件路路径
    private String fileFullName;
    // 文件大小
    private String fileSize;
    // 计算生成
    private String gitHubUrl;
    // 计算生成
    private String giteeUrl;

    // gitee Web IDE 编辑的页面
    private String giteeWebIdeEditUrl;

    // gitee 普通编辑界面
    private String giteeEditUrl;

    // editUrl;
    private String editUrl;

    /**
     * gitee 图片 CDN 加速地址
     */
    private String giteeRawUrl;

    private String filePathSubDocsFilePath;

    /**
     * 设置 ownImgUrl
     */
    private String ownImgUrl;

    public void setFilePathSubDocsFilePath(String filePathSubDocsFilePath) {
        this.filePathSubDocsFilePath = filePathSubDocsFilePath;
        this.ownImgUrl = "/getImage?cache=true&filePathSubDocsFilePath=" + this.filePathSubDocsFilePath;
    }

    public String getImageShowUrl() {
        return this.ownImgUrl;
    }

    public void setGiteeWebIdeEditUrl(String giteeWebIdeEditUrl) {
        this.giteeWebIdeEditUrl = giteeWebIdeEditUrl;
        if (Objects.nonNull(this.giteeUrl) && !giteeUrl.endsWith("svg")) {
            this.editUrl = giteeWebIdeEditUrl;
        }
    }

    public void setGiteeEditUrl(String giteeEditUrl) {
        this.giteeEditUrl = giteeEditUrl;
        if (Objects.nonNull(this.giteeUrl) && giteeUrl.endsWith("svg")) {
            this.editUrl = giteeEditUrl;
        }
    }
}
