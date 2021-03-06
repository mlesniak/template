package com.mlesniak.template.config;

import com.mlesniak.template.dao.ConfigDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class Config {
    private Logger log = LoggerFactory.getLogger(Config.class);
    private static final String FILENAME = "/config.properties";
    private final Properties properties;
    private static Config INSTANCE;
    private boolean databaseResolution = false;

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

    public Map<String, String> getConfig() {
        Map<String, String> map = new HashMap<>();
        for (String name : getKeys()) {
            map.put(name, get(name));
        }
        return map;
    }

    public void load() {
        log.debug("Loading configuration.");
        try {
            properties.load(Config.class.getResourceAsStream(FILENAME));
        } catch (IOException | NullPointerException e) {
            log.error("Unable to load config file from classpath. filename=" + FILENAME, e);
        }
    }

    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }

    public int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public void enableDatabaseResolution() {
        log.debug("Database resolution enabled.");
        databaseResolution = true;
    }

    public boolean isKeyDefined(String key) {
        return ConfigDao.get().isKeyDefined(key);
    }

    public void set(String key, String value) {
        if (!databaseResolution) {
            log.warn("Key not set. Database initalization not finished. key=" + key);
            return;
        }

        ConfigDao.get().update(key, value);
    }

    public String get(String key) {
        if (!databaseResolution) {
            return properties.getProperty(key);
        }
        return ConfigDao.get().get(key).getValue();
    }

    public void resetToConfigFile() {
        databaseResolution = false;
        ConfigDao.get().clear();
        ConfigDao.get().initializeFromPropertiesFile();
        databaseResolution = true;
    }

    public Set<String> getKeys() {
        return (Set<String>)(Set<?>)properties.keySet();
    }
}
