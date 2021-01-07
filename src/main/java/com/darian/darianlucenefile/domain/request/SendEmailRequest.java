package com.darian.darianlucenefile.domain.request;

import lombok.Data;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a> 
 * @date 2021/1/7  22:06
 */
@Data
public class SendEmailRequest {

    private String emailTo;

    private String emailTitle;

    private String emailBody;
}
