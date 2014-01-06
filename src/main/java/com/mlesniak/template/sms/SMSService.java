package com.mlesniak.template.sms;

import com.mlesniak.template.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import static com.mlesniak.template.config.ConfigKeys.*;

/**
 * Using Nexmo for sending SMS.
 */
public class SMSService {
    private Logger log = LoggerFactory.getLogger(SMSService.class);
    private static SMSService INSTANCE;

    public static SMSService get() {
        if (INSTANCE == null) {
            INSTANCE = new SMSService();
        }

        return INSTANCE;
    }

    public void sendSMS(String to, String text) {
        Config config = Config.get();
        String s = null;
        try {
            s = "https://rest.nexmo.com/sms/json?api_key=" + config.get(SMS_API) + "&api_secret=" +
                    config.get(SMS_SECRET) + "&from=" + config.get(SMS_FROM) + "&to=" + to + "&text=" +
                    URLEncoder.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("Unsupported encoding.", e);
        }

        URL url = null;
        try {
            url = new URL(s);
        } catch (MalformedURLException e) {
            log.error("Malformed url.", e);
        }

        try {
            URLConnection urlConnection = url.openConnection();
            urlConnection.connect();
        } catch (IOException e) {
            log.error("Unable to connect.", e);
        }

        log.info("Sent SMS. to=" + to + " msg='" + text + "'");
    }
}
