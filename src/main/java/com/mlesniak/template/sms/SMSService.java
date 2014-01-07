package com.mlesniak.template.sms;

import com.mlesniak.template.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
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
        URL url = null;
        try {
            url = generateURL(to, text, config);
        } catch (UnsupportedEncodingException e) {
            log.error("Unsupported encoding.", e);
            return;
        } catch (MalformedURLException e) {
            log.error("Malformed url.", e);
            return;
        }

        try {
            String result = getURLContent(url);
            System.out.println(result);
        } catch (IOException e) {
            log.error("Unable to connect.", e);
        }

        log.info("Sent SMS. to=" + to + " msg='" + text + "'");
    }

    private URL generateURL(String to, String text, Config config) throws UnsupportedEncodingException, MalformedURLException {
        URL url;
        String s = "https://rest.nexmo.com/sms/json?api_key=" + config.get(SMS_API) + "&api_secret=" +
                config.get(SMS_SECRET) + "&from=" + config.get(SMS_FROM) + "&to=" + to + "&text=" +
                URLEncoder.encode(text, "UTF-8");
        url = new URL(s);
        return url;
    }

    private String getURLContent(URL url) throws IOException {
        InputStream inputStream = url.openConnection().getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
            sb.append('\n');
        }
        reader.close();

        return sb.toString();
    }
}
