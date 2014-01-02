package com.mlesniak.template.email;

import com.mlesniak.template.config.Config;
import com.mlesniak.template.config.ConfigKeys;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailService  {
    private Logger log = LoggerFactory.getLogger(EmailService.class);
    private static EmailService INSTANCE;

    public static EmailService get() {
        if (INSTANCE == null) {
            INSTANCE = new EmailService();
        }

        return INSTANCE;
    }

    public void sendEmail(String to, String subject, String message) {
        Email email = new SimpleEmail();
        Config config = Config.get();

        try {
            email.setHostName(config.get(ConfigKeys.EMAIL_HOST));
            email.setSmtpPort(config.getInt(ConfigKeys.EMAIL_PORT));
            DefaultAuthenticator auth = new DefaultAuthenticator(
                    config.get(ConfigKeys.EMAIL_USERNAME), config.get(ConfigKeys.EMAIL_PASSWORD));
            email.setAuthenticator(auth);
            email.setSSLOnConnect(true);
            email.setFrom(config.get(ConfigKeys.EMAIL_FROM));
            email.setSubject(subject);
            email.setMsg(message);
            email.addTo(to);
            email.send();
            log.info("Email sent");
        } catch (EmailException e) {
            log.error("Error sending email. e=" + e.getMessage(), e);
        }
    }
}
