package com.mlesniak.template.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * JPA entity mirroring the logback table schema to allow for queries.
 */
@Entity
@Table(name = "logging_event")
public class LogDO implements Serializable {
    @Id
    @Column(name = "event_id")
    private long id;

    @Column(name = "timestmp")
    long timestamp;

    @Column(name = "formatted_message")
    private String formattedMessages;

    @Column(name = "logger_name")
    private String loggerName;

    @Column(name = "level_string")
    private String level;

    @Column(name = "thread_name")
    private String threadrName;

    @Column(name = "reference_flag")
    private int referenceFlag;

    @Column(name = "arg0")
    private String arg0;

    @Column(name = "arg1")
    private String arg1;

    @Column(name = "arg2")
    private String arg2;

    @Column(name = "arg3")
    private String arg3;

    @Column(name = "caller_filename")
    private String callerFilename;

    @Column(name = "caller_class")
    private String callerClass;

    @Column(name = "caller_method")
    private String callerMethod;

    @Column(name = "caller_line")
    private String callerLine;

    public long getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getFormattedMessages() {
        return formattedMessages;
    }

    public void setFormattedMessages(String formattedMessages) {
        this.formattedMessages = formattedMessages;
    }

    public String getLoggerName() {
        return loggerName;
    }

    public void setLoggerName(String loggerName) {
        this.loggerName = loggerName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getThreadrName() {
        return threadrName;
    }

    public void setThreadrName(String threadrName) {
        this.threadrName = threadrName;
    }

    public int getReferenceFlag() {
        return referenceFlag;
    }

    public void setReferenceFlag(int referenceFlag) {
        this.referenceFlag = referenceFlag;
    }

    public String getArg0() {
        return arg0;
    }

    public void setArg0(String arg0) {
        this.arg0 = arg0;
    }

    public String getArg1() {
        return arg1;
    }

    public void setArg1(String arg1) {
        this.arg1 = arg1;
    }

    public String getArg2() {
        return arg2;
    }

    public void setArg2(String arg2) {
        this.arg2 = arg2;
    }

    public String getArg3() {
        return arg3;
    }

    public void setArg3(String arg3) {
        this.arg3 = arg3;
    }

    public String getCallerFilename() {
        return callerFilename;
    }

    public void setCallerFilename(String callerFilename) {
        this.callerFilename = callerFilename;
    }

    public String getCallerClass() {
        return callerClass;
    }

    public void setCallerClass(String callerClass) {
        this.callerClass = callerClass;
    }

    public String getCallerMethod() {
        return callerMethod;
    }

    public void setCallerMethod(String callerMethod) {
        this.callerMethod = callerMethod;
    }

    public String getCallerLine() {
        return callerLine;
    }

    public void setCallerLine(String callerLine) {
        this.callerLine = callerLine;
    }

    @Override
    public String toString() {
        return "LogDO{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                ", formattedMessages='" + formattedMessages + '\'' +
                ", loggerName='" + loggerName + '\'' +
                ", level='" + level + '\'' +
                ", threadrName='" + threadrName + '\'' +
                ", referenceFlag=" + referenceFlag +
                ", arg0='" + arg0 + '\'' +
                ", arg1='" + arg1 + '\'' +
                ", arg2='" + arg2 + '\'' +
                ", arg3='" + arg3 + '\'' +
                ", callerFilename='" + callerFilename + '\'' +
                ", callerClass='" + callerClass + '\'' +
                ", callerMethod='" + callerMethod + '\'' +
                ", callerLine='" + callerLine + '\'' +
                '}';
    }
}
