package com.mlesniak.template.logging;

import ch.qos.logback.classic.Level;

import java.util.ArrayList;
import java.util.List;

public class LogFilter {
    private Level level;
    private String keyword;

    private LogFilter() {
        // Empty.
    }

    public static LogFilter start() {
        return new LogFilter();
    }

    public LogFilter addLevel(Level level) {
        this.level = level;
        return this;
    }

    public LogFilter addKeyword(String keyword) {
        this.keyword = keyword;
        return this;
    }

    public String build() {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT l FROM LogDO l ");

        List<String> attributeQueries = new ArrayList<>();
        if (level != null && level != Level.ALL) {
            attributeQueries.add(" l.level = '" + level.levelStr + "\'");
        }
        if (keyword != null) {
            attributeQueries.add(" l.formattedMessages like '%" + keyword + "%'");
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

        sb.append("Order by l.id desc");

        return sb.toString();
    }

    @Override
    public String toString() {
        return "LogFilter{" +
                "level=" + level +
                ", keyword='" + keyword + '\'' +
                '}';
    }
}
