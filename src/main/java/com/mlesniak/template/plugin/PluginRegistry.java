package com.mlesniak.template.plugin;

import org.apache.commons.lang3.StringUtils;

public enum PluginRegistry {
    HelloWorld("HelloWorld", HelloWorld.class)
    ;

    private String name;
    private Class iface;

    PluginRegistry(String name, Class iface) {
        this.name = name;
        this.iface = iface;
    }

    public String getName() {
        return name;
    }

    public Class getIface() {
        return iface;
    }

    public static PluginRegistry get(String name) {
        for (PluginRegistry plugin : values()) {
            if (StringUtils.equals(plugin.getName(), name)) {
                return plugin;
            }
        }

        return null;
    }

    public static PluginRegistry get(Class iface) {
        for (PluginRegistry plugin : values()) {
            if (iface == plugin.iface) {
                return plugin;
            }
        }

        return null;
    }
}
