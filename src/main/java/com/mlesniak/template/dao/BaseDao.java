package com.mlesniak.template.dao;

import com.mlesniak.template.config.Config;
import com.mlesniak.template.config.ConfigKeys;
import org.apache.log4j.lf5.util.StreamUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

        // Special handling for embedded h2Used.
        boolean h2Embedded = false;
        if (config.get(ConfigKeys.DATABASE_DRIVER).equals("org.h2.Driver")) {
            log.info("Adding configuration for h2.");
//            configuration.put("eclipselink.target-database", "Derby");
            h2Embedded = false;
        }

        factory = Persistence.createEntityManagerFactory("database", configuration);
        if (h2Embedded) {
            // Fill database.
            EntityManager em = factory.createEntityManager();
            InputStream stream = BaseDao.class.getClassLoader().getResourceAsStream("/h2Used.sql");
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                StreamUtils.copy(stream, out);
            } catch (IOException e) {
                log.error("Unable to read h2Used.sql.", e);
            }

            em.getTransaction().begin();
            String[] sqlStatements = new String(out.toByteArray()).split(";");
            for (String statement : sqlStatements) {
                if (org.apache.commons.lang3.StringUtils.isBlank(statement)) {
                    continue;
                }
                System.out.println(statement);
                em.createNativeQuery(statement).executeUpdate();
            }
            em.getTransaction().commit();
        }
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
