package com.mlesniak.template.dao;

import com.mlesniak.template.model.StatisticDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;

public class StatisticsDao extends BaseDao {
    private Logger log = LoggerFactory.getLogger(StatisticsDao.class);

    private static StatisticsDao INSTANCE;

    public static StatisticsDao get() {
        if (INSTANCE == null) {
            INSTANCE = new StatisticsDao();
        }

        return INSTANCE;
    }

    public void write(StatisticDO message) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(message);
        em.getTransaction().commit();
        em.close();
    }
}
