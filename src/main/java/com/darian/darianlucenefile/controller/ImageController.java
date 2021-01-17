package com.darian.darianlucenefile.controller;

import com.darian.darianlucenefile.domain.CustomerFile;
import com.darian.darianlucenefile.domain.request.GetCustomerFileRequest;
import com.darian.darianlucenefile.filter.ServerResponseFilterUtils;
import com.darian.darianlucenefile.service.CustomerFileService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ZeroCopyHttpOutputMessage;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.io.File;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a>
 * @date 2021/1/17  下午2:59
 */
@Controller
public class ImageController {


    @Resource
    private CustomerFileService customerFileService;

    @RequestMapping("/getImage")
    public Mono<Void> getImagesId(ServerHttpResponse response,
                                  GetCustomerFileRequest request) {
        String filePathSubDocsFilePath = request.getFilePathSubDocsFilePath();
        CustomerFile customerFile = customerFileService.doGetCustomerFile(filePathSubDocsFilePath, true);
        String filePath = customerFile.getFileFullName();

        File imageFile = new File(filePath);
        long lastModifiedLong = imageFile.lastModified();
        if (imageFile.exists()) {
            try {
                ZeroCopyHttpOutputMessage zeroCopyResponse = (ZeroCopyHttpOutputMessage) response;
                //response.getHeaders().setContentType(MediaType.IMAGE_PNG); // 这句话写不写都行
                if (filePath.endsWith("svg")) {
                    // svg 格式的需要特殊
                    response.getHeaders().add(HttpHeaders.CONTENT_TYPE, "image/svg+xml");
                } else {
                    response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE);
                }
                response.getHeaders().setLastModified(lastModifiedLong);
                return zeroCopyResponse.writeWith(imageFile, 0, imageFile.length());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
            }
        }
        return ServerResponseFilterUtils.errorResponseWrapper(response, "Spring boot：文件没有找到");
    }

}
