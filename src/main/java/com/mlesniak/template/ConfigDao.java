package com.mlesniak.template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;

public class ConfigDao extends BaseDao {
    private Logger log = LoggerFactory.getLogger(ConfigDao.class);

    private static ConfigDao INSTANCE;
    private long lastCacheUpdate;
    public static final int UPDATE_INTERVALL_SECONDS = 10;

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

    private ConfigDO get(Config.Key key, boolean doLog) {
        try {
            checkAndInvalidateCache();
            return getConfigForKey(key);
        } catch (RuntimeException exception) {
            if (doLog) {
                log.warn("Unable to get key. key=" + key.get());
            }
        }

        return null;
    }

    private ConfigDO getConfigForKey(Config.Key key) {
        EntityManager em = getEntityManager();
        String query = "SELECT c FROM ConfigDO c WHERE c.key = '" + key.get() + "'";
        ConfigDO configDO = em.createQuery(query, ConfigDO.class).getSingleResult();
        em.detach(configDO);
        em.close();
        return configDO;
    }

    public void resetCache() {
        lastCacheUpdate = 0;
        checkAndInvalidateCache();
    }

    private void checkAndInvalidateCache() {
        long now = System.currentTimeMillis();
        if ((now - lastCacheUpdate) / 1000 > UPDATE_INTERVALL_SECONDS) {
            // We have to reset the cache on all config objects before we query, since we do not want the 2nd-level
            // cache to return old object instances.
            log.debug("Invalidating JPA cache");
            getEntityManagerFactory().getCache().evict(ConfigDO.class);
            lastCacheUpdate = now;
        }
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
}
