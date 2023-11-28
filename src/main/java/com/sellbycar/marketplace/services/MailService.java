package com.sellbycar.marketplace.services;

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
     * @param emailTo      The email address to which the message will be sent.
     * @param subject      The subject of the email message.
     * @param templateName The name of the Thymeleaf template to be used as the email content.
     * @param context      The Thymeleaf context containing variables to be used in the template.
     * @throws MessagingException       If there is an issue with sending the email.
     */

    public void sendSimpleMessage(String emailTo, String subject, String templateName, Context context) throws MessagingException;

}
