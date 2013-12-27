package com.mlesniak.template.dao;

import com.mlesniak.template.logging.LogFilter;
import com.mlesniak.template.model.LogDO;
import com.mlesniak.template.statistic.StatisticCategory;
import com.mlesniak.template.statistic.StatisticService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.concurrent.Callable;


public class LogDao extends BaseDao {
    private Logger log = LoggerFactory.getLogger(LogDao.class);
    private static LogDao INSTANCE;
    public static int MAX_RESULTS = 1000;

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

    public List<LogDO> getLogByFilter(final LogFilter logFilter) {
        return StatisticService.collect(StatisticCategory.Database, "collecting log", new Callable<List<LogDO>>() {
            @Override
            public List<LogDO> call() throws Exception {
                EntityManager em = getEntityManager();
                TypedQuery<LogDO> query = em.createQuery(logFilter.build(), LogDO.class);
                query.setMaxResults(MAX_RESULTS);
                List<LogDO> logDOList = query.getResultList();
                for (LogDO logDO : logDOList) {
                    em.detach(logDO);
                }
                em.close();
                return logDOList;
            }
        });
    }
}
