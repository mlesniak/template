package com.mlesniak.template;

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
        storeNewKeysInDatabase();
    }

    private static void storeNewKeysInDatabase() {
        Config config = Config.get();
        log.info("Updating database");
        ConfigDao dao = ConfigDao.get();
        for (String stringKey : config.getDefinedKeys()) {
            Config.Key key = Config.Key.getKey(stringKey);
            if (dao.isKeyDefined(key)) {
                continue;
            }
            String value = config.get(key);
            dao.put(key, value);
            if (log.isDebugEnabled()) {
                log.debug("Storing key=" + key.get());
            }
        }

        // Everything updated. Use only database from now on.
        config.enableDatabaseResolution();
    }

    private static void initializeFactory() {
        Map<String, String> configuration = new HashMap<>();
        Config config = Config.get();
        configuration.put("javax.persistence.jdbc.driver", config.get(Config.Key.databaseDriver));
        configuration.put("javax.persistence.jdbc.url", config.get(Config.Key.databaseURL));
        configuration.put("javax.persistence.jdbc.username", config.get(Config.Key.databaseUsername));
        configuration.put("javax.persistence.jdbc.password", config.get(Config.Key.databasePassword));
        String ddlGeneration = config.get(Config.Key.databaseGeneration);
        if (ddlGeneration != null) {
            configuration.put("eclipselink.ddl-generation", ddlGeneration);
        }
        factory = Persistence.createEntityManagerFactory("database", configuration);
    }

    public EntityManager getEntityManager() {
        return factory.createEntityManager();
    }
}