package com.mlesniak.template.statistic;

import java.io.Serializable;

public class StatisticModel implements Serializable {
    private StatisticCategory category;
    private String keyword;
    private String startTime;
    private String endTime;

    public StatisticCategory getCategory() {
        return category;
    }

    public void setCategory(StatisticCategory category) {
        this.category = category;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "StatisticModel{" +
                "category=" + category +
                ", keyword='" + keyword + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }

}
