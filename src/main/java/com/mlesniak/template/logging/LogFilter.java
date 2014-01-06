package com.mlesniak.template.logging;

import ch.qos.logback.classic.Level;
import com.mlesniak.template.dao.StatisticDao;
import com.mlesniak.template.model.LogDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class LogFilter {
    private Logger log = LoggerFactory.getLogger(LogFilter.class);

    private Level level;
    private String keyword;
    private Long startTime;
    private Long endTime;

    private transient ThreadLocal<SimpleDateFormat> dateTimeFormat = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy/MM/dd HH:mm");
        }
    };

    private LogFilter() {
        // Empty.
    }

    public static LogFilter start() {
        return new LogFilter();
    }

    public LogFilter addStartTime(String start) {
        if (start == null) {
            return this;
        }

        try {
            Date date = dateTimeFormat.get().parse(start);
            startTime = date.getTime();
        } catch (ParseException e) {
            log.error("Unable to parse start time. startTime=" + start, e);
        }
        return this;
    }

    public LogFilter addEndTime(String end) {
        if (end == null) {
            return this;
        }

        try {
            Date date = dateTimeFormat.get().parse(end);
            endTime = date.getTime();
        } catch (ParseException e) {
            log.error("Unable to parse end time. endTime=" + end, e);
        }
        return this;
    }


    public LogFilter addLevel(Level level) {
        this.level = level;
        return this;
    }

    public LogFilter addKeyword(String keyword) {
        this.keyword = keyword;
        return this;
    }

    public TypedQuery<LogDO> build() {
        EntityManager em = StatisticDao.get().getEntityManager();

        StringBuffer sb = new StringBuffer();
        Map<String, Object> params = new HashMap<>();
        sb.append("SELECT l FROM LogDO l ");

        List<String> attributeQueries = new ArrayList<>();
        if (level != null && level != Level.ALL) {
            attributeQueries.add(" l.level = :level");
            params.put("level", level.levelStr);
        }
        if (keyword != null) {
            attributeQueries.add(" LOWER(l.formattedMessages) LIKE :keyword ");
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

        sb.append(" Order by l.id desc ");

        TypedQuery<LogDO> query = em.createQuery(sb.toString(), LogDO.class);
        for (String param : params.keySet()) {
            query.setParameter(param, params.get(param));
        }

        return query;
    }

    @Override
    public String toString() {
        return "LogFilter{" +
                "level=" + level +
                ", keyword='" + keyword + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
