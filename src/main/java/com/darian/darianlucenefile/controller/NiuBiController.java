package com.darian.darianlucenefile.controller;

import com.darian.darianlucenefile.constants.DocumentConstants;
import com.darian.darianlucenefile.domain.CustomerFile;
import com.darian.darianlucenefile.domain.CustomerResponse;
import com.darian.darianlucenefile.domain.request.AddIllegalIPSetRequest;
import com.darian.darianlucenefile.domain.request.GetCustomerFileRequest;
import com.darian.darianlucenefile.domain.request.RefreshWhiteIpListRequest;
import com.darian.darianlucenefile.domain.request.SearchJsonRequest;
import com.darian.darianlucenefile.domain.request.SendEmailRequest;
import com.darian.darianlucenefile.exception.CustomerRuntimeException;
import com.darian.darianlucenefile.filter.ServerResponseFilterUtils;
import com.darian.darianlucenefile.filter.ip.IPContainer;
import com.darian.darianlucenefile.service.CustomerFileService;
import com.darian.darianlucenefile.service.IPPortsWhiteService;
import com.darian.darianlucenefile.service.LuceneService;
import com.darian.darianlucenefile.service.ShellService;
import com.darian.darianlucenefile.utils.AssertUtils;
import com.darian.darianlucenefile.utils.MailUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ZeroCopyHttpOutputMessage;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a> 
 * @date 2020/4/12  21:41
 */
@Slf4j
@Controller("/")
public class NiuBiController {

    @Resource
    private LuceneService luceneService;

    @Resource
    private ShellService shellService;

    @Resource
    private CustomerFileService customerFileService;

    @Resource
    private IPPortsWhiteService ipPortsWhiteService;


    @ResponseBody
    @RequestMapping(value = "/addIllegalIPSet", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CustomerResponse addIllegalIPSet(AddIllegalIPSetRequest addIllegalIPSetRequest) {
        String illegalIP = addIllegalIPSetRequest.getIllegalIP();
        AssertUtils.assertNotBlank(illegalIP, "illegalIP must be not blank");
        IPContainer.ILLEGAL_IP_SET.add(illegalIP);
        return CustomerResponse.ok("success");
    }

    @ResponseBody
    @RequestMapping(value = "/search", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Mono<CustomerResponse> searchJson(SearchJsonRequest request) {
        return Mono.justOrEmpty(luceneService.search(request));
    }

    @ResponseBody
    @RequestMapping(value = "/refresh", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Mono<CustomerResponse> refresh() {

        shellService.doRestart();
        return Mono.justOrEmpty(CustomerResponse.ok("success "));
    }

    @RequestMapping(value = "/getCustomerFile", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Mono<CustomerResponse> getCustomerFile(GetCustomerFileRequest request) {

        String filePathSubDocsFilePath = request.getFilePathSubDocsFilePath();
        Boolean cache = request.getCache();
        log.debug("[TestRestController.getCustomerFile].filePathSubDocsFilePath:" + filePathSubDocsFilePath);

        AssertUtils.assertTrue(StringUtils.hasText(filePathSubDocsFilePath), "filePathSubDocsFilePath不能为空");

        try {
            filePathSubDocsFilePath = URLDecoder.decode(filePathSubDocsFilePath, DocumentConstants.UTF_8);
            log.debug("[TestRestController.getContent].filePathSubGitFilePath.decode:" + filePathSubDocsFilePath);
        } catch (UnsupportedEncodingException e) {
            throw new CustomerRuntimeException("查询文件，解码报错");
        }

        return Mono.justOrEmpty(CustomerResponse.ok(customerFileService.doGetCustomerFile(filePathSubDocsFilePath, cache)));
    }

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

    @RequestMapping(value = "/refreshWhiteIpList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Mono<CustomerResponse> refreshWhiteIpList(RefreshWhiteIpListRequest request) {

        String whiteIP = request.getWhiteIP();
        AssertUtils.assertTrue(StringUtils.hasText(whiteIP), "whiteIP不能为空");

        return Mono.justOrEmpty(CustomerResponse.ok(ipPortsWhiteService.refreshWhiteIpList(whiteIP)));
    }

    @RequestMapping(value = "/sendEmail", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Mono<CustomerResponse> sendEmail(SendEmailRequest request) {

        String emailTo = request.getEmailTo();
        String emailTitle = request.getEmailTitle();
        String emailBody = request.getEmailBody();

        AssertUtils.assertTrue(StringUtils.hasText(emailTo), "emailTo不能为空");
        AssertUtils.assertTrue(StringUtils.hasText(emailTitle), "emailTitle不能为空");
        AssertUtils.assertTrue(StringUtils.hasText(emailBody), "emailBody不能为空");

        MailUtils.send(emailTo, emailTitle, emailBody);
        return Mono.justOrEmpty(CustomerResponse.ok("邮件发送成功", null));
    }
}
