package com.mlesniak.template.model;

import com.mlesniak.template.statistics.StatisticCategory;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class StatisticDO implements Serializable {
    @Id
    @GeneratedValue
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatisticCategory category;

    @Column(nullable = false)
    private long timestamp;

    private String description;

    @Column(nullable = false)
    private long time;

    public long getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
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
                ", timestamp=" + timestamp +
                ", description='" + description + '\'' +
                ", time=" + time +
                '}';
    }

}
