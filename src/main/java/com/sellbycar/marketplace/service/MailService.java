package com.sellbycar.marketplace.service;

import jakarta.mail.MessagingException;
import org.thymeleaf.context.Context;

public interface MailService {

    /**
     * Sends a simple email message to the specified email address.
     * <p>
     * This method is responsible for sending a basic email message with the provided subject and text
     * to the specified email address. The implementation details may include utilizing an email service
     * or an SMTP server to deliver the message.
     *
     * @param emailTo The email address to which the message will be sent.
     * @param subject The subject of the email message.
     * @param text    The text content of the email message.
     * @throws IllegalArgumentException if any of the provided parameters (emailTo, subject, or text) is null.
     */

    public void sendSimpleMessage(String emailTo, String subject, String templateName, Context context) throws MessagingException;

    }
