package com.ctool.user.service;

import com.ctool.user.controller.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.concurrent.Callable;

/**
 * @Author: Kylinrix
 * @Date: 2019/1/10 15:22
 * @Email: Kylinrix@outlook.com
 * @Description:
 */
@Component
public class MailService {
    private static final Logger logger = LoggerFactory.getLogger(MailService.class);

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Value("${mail.fromMail.addr}")
    private String from;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendVerifyMail(int userId,String userName,String email){
        MimeMessage message = mailSender.createMimeMessage();

        try {
            //true表示需要创建一个multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(email);
            helper.setSubject("邮箱账号验证");
            Context context = new Context();
            context.setVariable("id", userId);
            context.setVariable("name",userName);

            String emailContent = templateEngine.process("emailTemplate", context);

            helper.setText(emailContent, true);
            mailSender.send(message);

            logger.info("验证邮件发送成功");
        } catch (MessagingException e) {
            logger.error("发送验证邮件时发生异常！", e);
        }
    }

}
