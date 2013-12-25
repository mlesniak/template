package com.mlesniak.template.email;

import com.mlesniak.template.config.Config;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class EmailService  {
    private Logger log = LoggerFactory.getLogger(EmailService.class);
    private static EmailService INSTANCE;

    public static EmailService get() {
        if (INSTANCE == null) {
            INSTANCE = new EmailService();
        }

        return INSTANCE;
    }

    public void sendEmail() {
        Email email = new SimpleEmail();
        Config config = Config.get();

        try {
            email.setHostName(config.get(Config.Key.emailHost));
            email.setSmtpPort(config.getInt(Config.Key.emailPort));
            DefaultAuthenticator auth = new DefaultAuthenticator(
                    config.get(Config.Key.emailUsername), config.get(Config.Key.emailPassword));
            email.setAuthenticator(auth);
            email.setSSLOnConnect(true);
            email.setFrom(config.get(Config.Key.emailFrom));
            email.setSubject("[mlesniak.com] Statistic (" + new Date() + ")");
            email.setMsg("Hello, world");
            email.addTo("mail@mlesniak.com");
            email.send();
            log.info("Email sent");
        } catch (EmailException e) {
            log.error("Error sending email. e=" + e.getMessage(), e);
        }
    }
}
