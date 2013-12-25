package com.mlesniak.template.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

public class Config {
    private Logger log = LoggerFactory.getLogger(Config.class);
    private static final String FILENAME = "/config.properties";
    private final Properties properties;
    private static Config INSTANCE;
    private boolean databaseResolution = false;



    public enum Key {
        allowSubmit(),
        showDefaultOptions(),

        // Database configuration.
        databaseDriver("database.driver"),
        databaseURL("database.url"),
        databaseUsername("database.user"),
        databasePassword("database.password"),
        databaseGeneration("database.generation"),

        defaultAdminUsername("database.admin.username"),
        defaultAdminPassword("database.admin.password"),
        defaultAdminEmail("database.admin.email"),
        defaultAdminRoles("database.admin.roles"),
        defaultAdmimLanguage("database.admin.language"),
        ;
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

        public static Key getKey(String value) {
            for (Key key : Key.values()) {
                if (key.get().equals(value)) {
                    return key;
                }
            }

            return null;
        }

    }
    private Config() {
        properties = new Properties();
        load();
    }

    public void init() {
        ConfigDao.get().init();
    }

    public static Config get() {
        if (INSTANCE == null) {
            INSTANCE = new Config();
        }

        return INSTANCE;
    }

    public Map<Key, String> getConfig() {
        Map<Key, String> map = new HashMap<>();
        for (Key key : Key.values()) {
            map.put(key, get(key));
        }
        return map;
    }

    public List<Key> getDefinedKeys() {
        return Arrays.asList(Key.values());
    }

    public void load() {
        log.debug("Loading configuration.");
        try {
            properties.load(Config.class.getResourceAsStream(FILENAME));
            // Check that only valid configuration keys are loaded.
            for (String key : properties.stringPropertyNames()) {
                if (Key.getKey(key) == null) {
                    log.warn("Key not defined in enum, ignoring. key=" + key);
                    properties.remove(key);
                }
            }
        } catch (IOException | NullPointerException e) {
            log.error("Unable to load config file from classpath. filename=" + FILENAME);
        }
    }

    public boolean getBoolean(Key key) {
        return Boolean.parseBoolean(get(key));
    }

    public void enableDatabaseResolution() {
        log.debug("Database resolution enabled.");
        databaseResolution = true;
    }

    public void set(Key key, String value) {
        if (!databaseResolution) {
            log.warn("Key not set. Database initalization not finished. key=" + key.get());
            return;
        }

        ConfigDao.get().update(key, value);
    }

    public String get(Key key) {
        if (!databaseResolution) {
            return properties.getProperty(key.get());
        }
        return ConfigDao.get().get(key).getValue();
    }

    public void resetToConfigFile() {
        databaseResolution = false;
        ConfigDao.get().clear();
        ConfigDao.get().initializeFromPropertiesFile();
        databaseResolution = true;
    }
}
