package com.sellbycar.marketplace.service.impl;

import com.sellbycar.marketplace.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.beans.factory.annotation.Value;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class MailSenderServiceImpl implements MailService {
    @Value("${spring.mail.username}")
    private String username;

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    public MailSenderServiceImpl(JavaMailSender javaMailSender,TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    public void sendSimpleMessage(String emailTo, String subject, String templateName, Context context) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        helper.setFrom(username);
        helper.setTo(emailTo);
        helper.setSubject(subject);

        String htmlContent = templateEngine.process(templateName, context);
        helper.setText(htmlContent, true);

        javaMailSender.send(mimeMessage);
    }
}
