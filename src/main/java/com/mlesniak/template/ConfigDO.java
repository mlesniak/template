package com.mlesniak.template;

import javax.persistence.*;


@Entity
@Table(name = "Config")
public class ConfigDO {
    @Id
    @GeneratedValue
    private long id;

    @Column(unique = true)
    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
