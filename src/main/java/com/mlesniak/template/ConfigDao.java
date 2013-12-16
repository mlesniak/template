package com.mlesniak.template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;

public class ConfigDao extends BaseDao {
    private Logger log = LoggerFactory.getLogger(ConfigDao.class);

    private static ConfigDao INSTANCE;

    public static ConfigDao get() {
        if (INSTANCE == null) {
            INSTANCE = new ConfigDao();
        }

        return INSTANCE;
    }

    public void init() {
        super.init();
    }

    public boolean isKeyDefined(Config.Key key) {
        return get(key, false) != null;
    }

    public ConfigDO get(Config.Key key) {
        return get(key, true);
    }

    public void put(Config.Key key, String value) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        ConfigDO configDO = new ConfigDO();
        configDO.setKey(key.get());
        configDO.setValue(value);
        em.persist(configDO);
        em.getTransaction().commit();
        em.close();
    }

    private ConfigDO get(Config.Key key, boolean doLog) {
        ConfigDO configDO = null;
        try {
            EntityManager em = getEntityManager();
            String query = "SELECT c FROM ConfigDO c WHERE c.key = '" + key.get() + "'";
            configDO = em.createQuery(query, ConfigDO.class).getSingleResult();
            em.close();
        } catch (RuntimeException exception) {
            if (doLog) {
                log.warn("Unable to get key. key=" + key.get());
            }
        }
        return configDO;
    }
}
