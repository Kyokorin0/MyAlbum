package com.kyoko.myalbum.email;

import com.kyoko.myalbum.enumCode.EnumCode;
import com.kyoko.myalbum.exception.MyException;
import com.kyoko.myalbum.properties.ProjProperties;
import com.kyoko.myalbum.result.Result;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author young
 * @create 2023/5/8 23:36
 * @Description
 */
@Service
@AllArgsConstructor
public class EmailServ implements EmailSender {
    private final static Logger logger = LoggerFactory.getLogger(EmailServ.class);
    private final JavaMailSender mailSender;
    private final ProjProperties projProperties;

    @Override
    @Async
    public void send(String to, String email) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            helper.setText(email, true);
            helper.setTo(to);//目标邮箱地址
            helper.setSubject("邮箱验证请求");
            helper.setFrom(projProperties.getMyEmail());
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            logger.error("邮件发送失败", e);
            throw new MyException(Result.builder()
                    .code(EnumCode.INTERNAL_SERVER_ERROR.getValue())
                    .msg("邮件发送失败，请重试！")
                    .data(e.getMessage())
                    .build().toJson());
        }
    }
}
