package com.darian.darianlucenefile.service;

import com.darian.darianlucenefile.domain.CustomerFile;
import com.darian.darianlucenefile.domain.CustomerResponse;
import com.darian.darianlucenefile.domain.request.SearchJsonRequest;
import com.darian.darianlucenefile.repository.CustomerFileRepository;
import com.darian.darianlucenefile.repository.LuceneRepository;
import com.darian.darianlucenefile.utils.AssertUtils;
import com.darian.darianlucenefile.utils.CacheFunctionUtils;
import com.darian.darianlucenefile.utils.CustomerImgUtils;
import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.googlecode.concurrentlinkedhashmap.Weighers;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a> 
 * @date 2020/4/18  0:03
 */
@Service
public class LuceneService {

    @Resource
    private LuceneRepository luceneRepository;

    @Resource
    private CustomerFileRepository customerFileRepository;

    ConcurrentLinkedHashMap<String, CustomerResponse> searchConcurrentLinkedHashMap =
            new ConcurrentLinkedHashMap.Builder<String, CustomerResponse>()
                    .maximumWeightedCapacity(3)
                    .weigher(Weighers.singleton())
                    .build();

    public CustomerResponse search(SearchJsonRequest request) {

        String param = request.getParam();
        boolean cache = Boolean.TRUE.equals(request.getCache());

        param = param.replaceAll("-", "");
        param = param.replaceAll("\\+", "");

        AssertUtils.assertTrue(StringUtils.hasText(param), "查询参数不能等于空");

        String finalParam = param;

        request.setParam(finalParam);

        CustomerResponse response = CacheFunctionUtils.getCacheOrNotCache(cache,
                () -> searchConcurrentLinkedHashMap.get(finalParam),
                () -> this.doSearch(finalParam),
                value -> searchConcurrentLinkedHashMap.put(finalParam, value));

        response.setRequest(request);
        return response;
    }

    private CustomerResponse doSearch(String param) {
        AssertUtils.assertTrue(StringUtils.hasText(param), "查询参数不能等于空");



        List<CustomerFile> luceneResponseList = luceneRepository.multiFiledQueryParser(param);

        for (CustomerFile luceneCustomerFile : luceneResponseList) {
            String filePathSubDocsFilePath = luceneCustomerFile.getFilePathSubDocsFilePath();

            // 查询内存仓库，补齐
            CustomerFile responseCustomerFile = customerFileRepository.findCopyCustomerFile(filePathSubDocsFilePath);
            luceneCustomerFile.setGiteeUrl(responseCustomerFile.getGiteeUrl());
            luceneCustomerFile.setGiteeRawUrl(responseCustomerFile.getGiteeRawUrl());
            luceneCustomerFile.setGiteeEditUrl(responseCustomerFile.getGiteeEditUrl());
            luceneCustomerFile.setGitHubUrl(responseCustomerFile.getGitHubUrl());
            luceneCustomerFile.setGiteeWebIdeEditUrl(responseCustomerFile.getGiteeWebIdeEditUrl());

            // 特殊处理，图片的 contentDetail ，把图片链接放进去
            if (CustomerImgUtils.isImg(responseCustomerFile.getFileSimpleName())) {
                // 图片的话设置它的 图片链接放进去
                String imageShowUrl = responseCustomerFile.getImageShowUrl();
                luceneCustomerFile.setContentDetail("<img src=" + imageShowUrl + " >");
            }
        }

        String notifyMsg = generatorSearchNotifyMsg(luceneResponseList);

        return CustomerResponse.ok(notifyMsg, luceneResponseList);
    }

    private static String generatorSearchNotifyMsg(List<CustomerFile> luceneResponseList) {
        String notifyMsg;
        if (luceneResponseList.isEmpty()) {
            notifyMsg = "没有找到对应的文章!!! ";
        } else {
            notifyMsg = "本页显示[" + luceneResponseList.size() + "]条";
        }
        return notifyMsg;
    }
}
