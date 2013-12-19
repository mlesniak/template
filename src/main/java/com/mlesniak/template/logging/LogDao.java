package com.mlesniak.template.logging;

import com.mlesniak.template.dao.BaseDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;


public class LogDao extends BaseDao {
    private Logger log = LoggerFactory.getLogger(LogDao.class);

    private static LogDao INSTANCE;

    public static LogDao get() {
        if (INSTANCE == null) {
            INSTANCE = new LogDao();
        }

        return INSTANCE;
    }

    public LogDO getLogById(long id) {
        EntityManager em = getEntityManager();
        String query = "SELECT c FROM LogDO c WHERE c.id = " + id;
        LogDO logDO = em.createQuery(query, LogDO.class).getSingleResult();
        em.detach(logDO);
        em.close();
        return logDO;
    }
}
