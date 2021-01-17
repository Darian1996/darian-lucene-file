package com.darian.darianlucenefile.controller;

import com.darian.darianlucenefile.constants.DocumentConstants;
import com.darian.darianlucenefile.domain.CustomerResponse;
import com.darian.darianlucenefile.domain.request.AddIllegalIPSetRequest;
import com.darian.darianlucenefile.domain.request.GetCustomerFileRequest;
import com.darian.darianlucenefile.domain.request.RefreshWhiteIpListRequest;
import com.darian.darianlucenefile.domain.request.SearchJsonRequest;
import com.darian.darianlucenefile.domain.request.SendEmailRequest;
import com.darian.darianlucenefile.exception.CustomerRuntimeException;
import com.darian.darianlucenefile.filter.ip.IPContainer;
import com.darian.darianlucenefile.service.CustomerFileService;
import com.darian.darianlucenefile.service.IPPortsWhiteService;
import com.darian.darianlucenefile.service.LuceneService;
import com.darian.darianlucenefile.service.ShellService;
import com.darian.darianlucenefile.utils.AssertUtils;
import com.darian.darianlucenefile.utils.MailUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a> 
 * @date 2020/4/12  21:41
 */
@Slf4j
@RestController
public class NiuBiRestController {

    @Resource
    private LuceneService luceneService;

    @Resource
    private ShellService shellService;

    @Resource
    private CustomerFileService customerFileService;

    @Resource
    private IPPortsWhiteService ipPortsWhiteService;


    @GetMapping("/addIllegalIPSet")
    public CustomerResponse addIllegalIPSet(AddIllegalIPSetRequest addIllegalIPSetRequest) {
        String illegalIP = addIllegalIPSetRequest.getIllegalIP();
        AssertUtils.assertNotBlank(illegalIP, "illegalIP must be not blank");
        IPContainer.ILLEGAL_IP_SET.add(illegalIP);
        return CustomerResponse.ok("success");
    }

    @GetMapping("/search")
    public Mono<CustomerResponse> searchJson(SearchJsonRequest request) {
        return Mono.justOrEmpty(luceneService.search(request));
    }

    @GetMapping("/refresh")
    public Mono<CustomerResponse> refresh() {

        shellService.doRestart();
        return Mono.justOrEmpty(CustomerResponse.ok("success "));
    }

    @GetMapping("/getCustomerFile")
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


    @GetMapping("/refreshWhiteIpList")
    public Mono<CustomerResponse> refreshWhiteIpList(RefreshWhiteIpListRequest request) {

        String whiteIP = request.getWhiteIP();
        AssertUtils.assertTrue(StringUtils.hasText(whiteIP), "whiteIP不能为空");

        return Mono.justOrEmpty(CustomerResponse.ok(ipPortsWhiteService.refreshWhiteIpList(whiteIP)));
    }


    @GetMapping("/sendEmail")
    public Mono<CustomerResponse> sendEmail(SendEmailRequest request) {

        String emailTo = request.getEmailTo();
        String emailTitle = request.getEmailTitle();
        String emailBody = request.getEmailBody();

        AssertUtils.assertNotBlank(emailTo, "emailTo不能为空");
        AssertUtils.assertNotBlank(emailTitle, "emailTitle不能为空");
        AssertUtils.assertNotBlank(emailBody, "emailBody不能为空");

        MailUtils.send(emailTo, emailTitle, emailBody);
        return Mono.justOrEmpty(CustomerResponse.ok("邮件发送成功", null));
    }
}
