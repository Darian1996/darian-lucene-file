package com.darian.darianlucenefile.utils;

import com.darian.darianlucenefile.exception.CustomerRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a> 
 * @date 2020/5/22  1:53
 */
@Slf4j
public class MailUtils {

    public static Properties properties;

    public static void sendMail(String title, String content) {
        String to = properties.getProperty("mail.to");
        String token = properties.getProperty("mail.token");

        if (StringUtils.isEmpty(token)) {
            log.error("[mail.token] 没有配置，请配置.............");
            return;
        }

        send(to, title, content);

    }

    public static void send(String to, String title, String content) {
        try {
            String from = properties.getProperty("mail.username");
            String token = properties.getProperty("mail.token");

            AssertUtils.assertTrue(StringUtils.hasText(token), "服务器[mail.token]没有配置");

            Session session = Session.getDefaultInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(from, token);
                }
            });
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setRecipient(Message.RecipientType.CC, new InternetAddress(from));
            message.setSubject(title);
            message.setContent(content, "text/html;charset=UTF-8");
            Transport transport = session.getTransport();
            transport.connect(from, token);
            Transport.send(message);
        } catch (MessagingException e) {
            log.error("发送系统启动消息异常：", e);
            throw new CustomerRuntimeException("发送邮件异常... ");
        }
    }

    public static void main(String[] args) {
        properties = PropertiesUtils.load("/mail.properties");
    }
}
