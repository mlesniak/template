package com.mlesniak.template.dao;

import com.mlesniak.template.model.StatisticDO;
import com.mlesniak.template.statistic.StatisticCategory;
import com.mlesniak.template.statistic.StatisticFilter;
import com.mlesniak.template.statistic.StatisticService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
                        TypedQuery<String> query = createDescriptionQuery(em);
                        List<String> descList = query.getResultList();
                        em.close();
                        return descList;
                    }

                    private TypedQuery<String> createDescriptionQuery(EntityManager em) {
                        StringBuffer sb = new StringBuffer();
                        Map<String, Object> params = new HashMap<>();

                        sb.append("SELECT DISTINCT l.description FROM StatisticDO l WHERE ");
                        if (category != null && category != StatisticCategory.All) {
                            sb.append("l.category = :category AND ");
                            params.put("category", category);
                        }

                        sb.append("l.description like :description ORDER BY l.description ASC");
                        params.put("description", "%" + descStart.toLowerCase() + "%");

                        TypedQuery<String> query = em.createQuery(sb.toString(), String.class);
                        for (String param : params.keySet()) {
                            query.setParameter(param, params.get(param));
                        }

                        return query;
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
