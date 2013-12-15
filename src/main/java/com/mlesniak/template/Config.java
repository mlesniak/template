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
        allowSubmit(),

        // Database configuration.
        databaseDriver("database.driver"),
        databaseURL("database.url"),
        databaseUsername("database.user"),
        databasePassword("database.password"),
        databaseGeneration("database.generation");

        private final String path;

        Key() {
            this.path = null;
        }

        Key(String path) {
            this.path = path;
        }

        public String get() {
            if (path == null) {
                return this.toString();
            }

            return path;
        }
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
        return Boolean.parseBoolean(properties.getProperty(key.get()));
    }

    public String get(Key key) {
        return properties.getProperty(key.get());
    }
}
