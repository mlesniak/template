package com.mlesniak.template.logging;

import com.mlesniak.template.dao.BaseDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.util.List;


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

    public List<LogDO> getLogByFilter(LogFilter logFilter) {
        EntityManager em = getEntityManager();
        List<LogDO> logDOList = em.createQuery(logFilter.build(), LogDO.class).getResultList();
        for (LogDO logDO : logDOList) {
            em.detach(logDO);
        }
        em.close();
        return logDOList;
    }
}
