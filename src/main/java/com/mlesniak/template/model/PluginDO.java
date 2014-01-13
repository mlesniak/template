package com.mlesniak.template.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class PluginDO implements Serializable {
    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private long timestamp;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private byte[] jar;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getJar() {
        return jar;
    }

    public void setJar(byte[] jar) {
        this.jar = jar;
    }

    @Override
    public String toString() {
        return "PluginDO{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                ", name='" + name + '\'' +
                ", jar.size=" + jar.length +
                '}';
    }
}
