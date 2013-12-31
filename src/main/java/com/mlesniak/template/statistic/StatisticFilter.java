package com.mlesniak.template.statistic;

import com.mlesniak.template.dao.StatisticDao;
import com.mlesniak.template.model.StatisticDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class StatisticFilter {
    private Logger log = LoggerFactory.getLogger(StatisticFilter.class);
    
    private StatisticCategory category;
    private String keyword;
    private Long startTime;
    private Long endTime;

    private transient ThreadLocal<SimpleDateFormat> dateTimeFormat = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy/MM/dd HH:mm");
        }
    };

    private StatisticFilter() {
        // Empty.
    }

    public static StatisticFilter start() {
        return new StatisticFilter();
    }

    public StatisticFilter addStartTime(String start) {
        if (start == null) {
            return this;
        }

        try {
            Date date = dateTimeFormat.get().parse(start);
            startTime = date.getTime();
        } catch (ParseException e) {
            log.error("Unable to parse start time. startTime=" + start);
        }
        return this;
    }

    public StatisticFilter addEndTime(String end) {
        if (end == null) {
            return this;
        }

        try {
            Date date = dateTimeFormat.get().parse(end);
            endTime = date.getTime();
        } catch (ParseException e) {
            log.error("Unable to parse end time. endTime=" + end);
        }
        return this;
    }


    public StatisticFilter addCategory(StatisticCategory level) {
        this.category = level;
        return this;
    }

    public StatisticFilter addKeyword(String keyword) {
        this.keyword = keyword;
        return this;
    }

    public TypedQuery<StatisticDO> build() {
        EntityManager em = StatisticDao.get().getEntityManager();

        StringBuffer sb = new StringBuffer();
        Map<String, Object> params = new HashMap<>();
        sb.append("SELECT l FROM StatisticDO l ");

        List<String> attributeQueries = new ArrayList<>();
        if (category != null && category != StatisticCategory.All) {
            attributeQueries.add(" l.category = :category");
            params.put("category", category);
        }
        if (keyword != null) {
            attributeQueries.add(" LOWER(l.description) LIKE :keyword ");
            params.put("keyword", "%" + keyword.toLowerCase() + "%");
        }
        if (startTime != null) {
            attributeQueries.add(" l.timestamp >= :startTime");
            params.put("startTime", startTime);
        }
        if (endTime != null) {
            attributeQueries.add(" l.timestamp <= :endTime");
            params.put("endTime", endTime);
        }

        if (!attributeQueries.isEmpty()) {
            sb.append(" WHERE ");
            for (int i = 0; i < attributeQueries.size(); i++) {
                if (i > 0) {
                    sb.append(" AND ");
                }
                sb.append(attributeQueries.get(i));
            }
        }

        sb.append(" Order by l.id asc ");

        TypedQuery<StatisticDO> query = em.createQuery(sb.toString(), StatisticDO.class);
        for (String param : params.keySet()) {
            query.setParameter(param, params.get(param));
        }

        return query;
    }

    @Override
    public String toString() {
        return "LogFilter{" +
                "category=" + category +
                ", keyword='" + keyword + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
