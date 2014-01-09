package com.mlesniak.template.dao;

import com.mlesniak.template.model.PluginDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class PluginDao extends BaseDao {
    private Logger log = LoggerFactory.getLogger(PluginDao.class);

    private static PluginDao INSTANCE;

    public static PluginDao get() {
        if (INSTANCE == null) {
            INSTANCE = new PluginDao();
        }

        return INSTANCE;
    }

    public void write(PluginDO pluginDO) {
        log.debug("Persisting: " + pluginDO);
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(pluginDO);
        em.getTransaction().commit();
        em.close();
    }

    public PluginDO getByName(String name) {
        EntityManager em = getEntityManager();
        TypedQuery<PluginDO> query = em.createQuery("SELECT u FROM PluginDO u where u.name = :name", PluginDO.class);
        query.setParameter("name", name);
        List<PluginDO> resultList = query.getResultList();
        em.close();

        if (resultList.isEmpty()) {
            return null;
        }
        return resultList.get(0);
    }
}
