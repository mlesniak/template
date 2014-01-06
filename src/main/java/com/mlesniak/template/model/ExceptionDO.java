package com.mlesniak.template.model;

import javax.persistence.*;

@Entity
@Table(name = "logging_event")
public class ExceptionDO {
    @ManyToOne
    @JoinColumn(name = "event_id")
    private LogDO logDO;

    @Column(name = "i")
    private int row;

    @Column(name = "trace_line")
    private String line;

    public LogDO getLogDO() {
        return logDO;
    }

    public void setLogDO(LogDO logDO) {
        this.logDO = logDO;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    @Override
    public String toString() {
        return "ExceptionDO{" +
                "logDO=" + logDO.getId() +
                ", row=" + row +
                ", line='" + line + '\'' +
                '}';
    }
}
