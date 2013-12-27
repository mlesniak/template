package com.mlesniak.template.dao;

import com.mlesniak.template.model.StatisticDO;
import com.mlesniak.template.statistics.StatisticCategory;
import com.mlesniak.template.statistics.StatisticFilter;
import com.mlesniak.template.statistics.StatisticService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.concurrent.Callable;

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

    public List<StatisticDO> getStatisticByFilter(final StatisticFilter filter) {
        return StatisticService.collect(StatisticCategory.Database, "collecting statistic",
                new Callable<List<StatisticDO>>() {
                    @Override
                    public List<StatisticDO> call() throws Exception {
                        EntityManager em = getEntityManager();
                        TypedQuery<StatisticDO> query = em.createQuery(filter.build(), StatisticDO.class);
                        List<StatisticDO> logDOList = query.getResultList();
                        for (StatisticDO logDO : logDOList) {
                            em.detach(logDO);
                        }
                        em.close();
                        return logDOList;
                    }
                });
    }
}
