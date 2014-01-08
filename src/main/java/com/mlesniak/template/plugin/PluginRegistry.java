package com.mlesniak.template.plugin;

import org.apache.commons.lang3.StringUtils;

public enum PluginRegistry {
    HelloWorld("HelloWorld", "com.mlesniak.template.plugin.HelloWorldImpl", HelloWorld.class)
    ;

    private String name;
    private String mainClass;
    private Class iface;

    PluginRegistry(String name, String mainClass, Class iface) {
        this.name = name;
        this.mainClass = mainClass;
        this.iface = iface;
    }

    public String getMainClass() {
        return mainClass;
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
