package com.mlesniak.template.dao;

import com.mlesniak.template.logging.LogFilter;
import com.mlesniak.template.model.LogDO;
import com.mlesniak.template.statistic.StatisticCategory;
import com.mlesniak.template.statistic.StatisticService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.concurrent.Callable;


public class LogDao extends BaseDao {
    private Logger log = LoggerFactory.getLogger(LogDao.class);
    private static LogDao INSTANCE;
    public static int MAX_RESULTS = 250;

    public static LogDao get() {
        if (INSTANCE == null) {
            INSTANCE = new LogDao();
        }

        return INSTANCE;
    }

    public List<LogDO> getLogByFilter(final LogFilter logFilter) {
        return StatisticService.collect(StatisticCategory.Database, "collecting log", new Callable<List<LogDO>>() {
            @Override
            public List<LogDO> call() throws Exception {
                EntityManager em = getEntityManager();
                TypedQuery<LogDO> query = logFilter.build();
                query.setMaxResults(MAX_RESULTS);
                List<LogDO> logDOList = query.getResultList();
                for (LogDO logDO : logDOList) {
                    em.detach(logDO);
                    attachException(em, logDO);
                }
                em.close();
                return logDOList;
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void attachException(EntityManager em, LogDO logDO) {
        Query query = em.createNativeQuery("SELECT e.trace_line FROM logging_event_exception e where e.event_id = " +
                logDO.getId() + " ORDER by e.i asc");

        StringBuffer sb = new StringBuffer();
        for (String line : (List<String>) query.getResultList()) {
            sb.append(line);
            sb.append("\n");
        }

        // Ignore empty exceptions with a single newline.
        if (sb.length() > 1) {
            logDO.setException(sb.toString());
        }
    }
}
