package com.mlesniak.template.statistic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public String build() {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT l FROM StatisticDO l ");

        List<String> attributeQueries = new ArrayList<>();
        if (category != null && category != StatisticCategory.All) {
            attributeQueries.add(" l.category = " + StatisticCategory.class.getCanonicalName() + "." +
                    category.toString());
        }
        if (keyword != null) {
            attributeQueries.add(" LOWER(l.description) LIKE '%" + keyword.toLowerCase() + "%' ");
        }
        if (startTime != null) {
            attributeQueries.add(" l.timestamp >= " + startTime);
        }
        if (endTime != null) {
            attributeQueries.add(" l.timestamp <= " + endTime);
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

        return sb.toString();
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
