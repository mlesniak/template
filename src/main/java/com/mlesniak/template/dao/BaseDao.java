package com.mlesniak.template.dao;

import com.mlesniak.template.config.Config;
import com.mlesniak.template.config.ConfigKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public class BaseDao {
    private static Logger log = LoggerFactory.getLogger(BaseDao.class);
    protected static EntityManagerFactory factory;

    protected void init() {
        if (factory != null) {
            return;
        }

        initializeFactory();
        storeNewDefaultKeysInDatabase();
        checkAndDoReset();
    }

    private void checkAndDoReset() {
        if (Config.get().getBoolean(ConfigKeys.RESET)) {
            log.warn("Resetting to default values.");
            Config.get().resetToConfigFile();
        }

        // Everything updated. Use only database from now on.
        Config.get().enableDatabaseResolution();
    }

    private static void initializeFactory() {
        Map<String, String> configuration = new HashMap<>();
        Config config = Config.get();
        configuration.put("javax.persistence.jdbc.driver", config.get(ConfigKeys.DATABASE_DRIVER));
        configuration.put("javax.persistence.jdbc.url", config.get(ConfigKeys.DATABASE_URL));
        configuration.put("javax.persistence.jdbc.user", config.get(ConfigKeys.DATABASE_USER));
        configuration.put("javax.persistence.jdbc.password", config.get(ConfigKeys.DATABASE_PASSWORD));
        String ddlGeneration = config.get(ConfigKeys.DATABASE_GENERATION);
        if (ddlGeneration != null) {
            configuration.put("eclipselink.ddl-generation", ddlGeneration);
        }
        factory = Persistence.createEntityManagerFactory("database", configuration);
    }

    protected static void storeNewDefaultKeysInDatabase() {
        Config config = Config.get();
        log.debug("Updating database");
        ConfigDao dao = ConfigDao.get();
        for (String key : config.getKeys()) {
            if (dao.isKeyDefined(key)) {
                continue;
            }
            String value = config.get(key);
            dao.put(key, value);
            if (log.isDebugEnabled()) {
                log.debug("Storing key=" + key);
            }
        }
    }

    public EntityManager getEntityManager() {
        return factory.createEntityManager();
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return factory;
    }
}
