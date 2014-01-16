package com.mlesniak.template.model;

import javax.persistence.*;


@Entity
@Table(name = "Config")
public class ConfigDO {
    @Id
    @GeneratedValue
    private long id;

    @Column(unique = true)
    private String identifier;
    private String value;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String key) {
        this.identifier = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ConfigDO{" +
                "id=" + id +
                ", key='" + identifier + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
