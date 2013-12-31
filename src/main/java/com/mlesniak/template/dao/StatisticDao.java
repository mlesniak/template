package com.mlesniak.template.dao;

import com.mlesniak.template.model.StatisticDO;
import com.mlesniak.template.statistic.StatisticCategory;
import com.mlesniak.template.statistic.StatisticFilter;
import com.mlesniak.template.statistic.StatisticService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.concurrent.Callable;

public class StatisticDao extends BaseDao {
    private Logger log = LoggerFactory.getLogger(StatisticDao.class);

    private static StatisticDao INSTANCE;

    public static StatisticDao get() {
        if (INSTANCE == null) {
            INSTANCE = new StatisticDao();
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

    public List<String> getAvailableDesciption(final StatisticCategory category, final String descStart) {
        return StatisticService.collect(StatisticCategory.Database, "collecting available descriptions",
                new Callable<List<String>>() {
                    @Override
                    public List<String> call() throws Exception {
                        EntityManager em = getEntityManager();
                        TypedQuery<String> query = em.createQuery(createDescriptionQuery(), String.class);
                        List<String> descList = query.getResultList();
                        em.close();
                        return descList;
                    }

                    private String createDescriptionQuery() {
                        StringBuffer sb = new StringBuffer();

                        sb.append("SELECT DISTINCT l.description FROM StatisticDO l WHERE ");
                        if (category != null && category != StatisticCategory.All) {
                            sb.append("l.category = " + StatisticCategory.class.getCanonicalName() + "." + category);
                            sb.append(" AND ");
                        }
                        sb.append("l.description like \"%" + descStart + "%\"");
                        sb.append(" ORDER BY l.description ASC");
                        return sb.toString();
                    }
                });
    }

    public List<StatisticDO> getStatisticByFilter(final StatisticFilter filter) {
        return StatisticService.collect(StatisticCategory.Database, "collecting statistic",
                new Callable<List<StatisticDO>>() {
                    @Override
                    public List<StatisticDO> call() throws Exception {
                        EntityManager em = getEntityManager();
                        TypedQuery<StatisticDO> query = filter.build();
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
