package com.mlesniak.template.logging;

import ch.qos.logback.classic.Level;

import java.io.Serializable;

public class LogModel implements Serializable {
    private Level level;
    private String keyword;

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public String toString() {
        return "LogModel{" +
                "level=" + level +
                ", keyword='" + keyword + '\'' +
                '}';
    }
}
