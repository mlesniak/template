package com.mlesniak.template.model;

import com.mlesniak.template.statistics.StatisticCategory;

import javax.persistence.*;

@Entity
public class StatisticDO {
    @Id
    @GeneratedValue
    private long id;

    @Enumerated(EnumType.STRING)
    private StatisticCategory category;

    private String description;

    private long time;

    public long getId() {
        return id;
    }

    public StatisticCategory getCategory() {
        return category;
    }

    public void setCategory(StatisticCategory category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "StatisticDO{" +
                "id=" + id +
                ", category=" + category +
                ", description='" + description + '\'' +
                ", time=" + time +
                '}';
    }
}
