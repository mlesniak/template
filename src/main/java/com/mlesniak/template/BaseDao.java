package com.mlesniak.template;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseDao {
    protected final EntityManagerFactory factory;

    public BaseDao() {
        Map<String, String> configuration = new HashMap<>();
        Config config = Config.get();
        configuration.put("javax.persistence.jdbc.driver", config.get(Config.Key.databaseDriver));
        configuration.put("javax.persistence.jdbc.url", config.get(Config.Key.databaseURL));
        configuration.put("javax.persistence.jdbc.username", config.get(Config.Key.databaseUsername));
        configuration.put("javax.persistence.jdbc.password", config.get(Config.Key.databasePassword));

        // Only add this if the
        String ddlGeneration = config.get(Config.Key.databaseGeneration);
        if (ddlGeneration != null) {
            configuration.put("eclipselink.ddl-generation", ddlGeneration);
        }

        factory = Persistence.createEntityManagerFactory("database", configuration);
    }

    protected EntityManager getEntityManager() {
        return factory.createEntityManager();
    }
}
