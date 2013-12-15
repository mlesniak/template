package com.mlesniak.template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

public class Config {
    private Logger log = LoggerFactory.getLogger(Config.class);
    private static final String FILENAME = "/config.properties";
    private final Properties properties;
    private static Config INSTANCE;

    public enum Key {
        allowSubmit;
    }

    private Config() {
        properties = new Properties();
        load();
    }

    public static Config get() {
        if (INSTANCE == null) {
            INSTANCE = new Config();
        }

        return INSTANCE;
    }

    public void load() {
        try {
            properties.load(Config.class.getResourceAsStream(FILENAME));
        } catch (IOException | NullPointerException e) {
            log.error("Unable to load config file from classpath. filename=" + FILENAME);
        }
    }

    public boolean getBoolean(Key key) {
        return Boolean.parseBoolean(properties.getProperty(key.toString()));
    }

    public String get(Key key) {
        return properties.getProperty(key.toString());
    }
}
