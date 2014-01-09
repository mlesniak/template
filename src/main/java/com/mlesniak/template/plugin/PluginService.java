package com.mlesniak.template.plugin;

import com.mlesniak.template.config.Config;
import com.mlesniak.template.config.ConfigKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

public class PluginService {
    private Logger log = LoggerFactory.getLogger(PluginService.class);
    private static PluginService INSTANCE;

    // Map from interface to loaded class.
    private Map<Class, Class> cache = new HashMap<>();

    public static PluginService get() {
        if (INSTANCE == null) {
            INSTANCE = new PluginService();
        }

        return INSTANCE;
    }

    public void resetCache() {
        cache.clear();
    }

    @SuppressWarnings("unchecked")
    public <T> T getPlugin(Class<T> iface) {
        Class clazz = null;
        if (cache.containsKey(iface)) {
            clazz = cache.get(iface);
        } else {
            clazz = loadClass(iface);
        }

        try {
            return (T) (clazz.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("Unable to load plugin. plugin=" + iface.getSimpleName(), e);
        }

        return null;
    }

    private Class loadClass(Class plugin) {
        String name = plugin.getSimpleName();
        Config config = Config.get();
        String pluginFile = config.get("plugin." + name + ".file");
        String pluginMainClass = config.get("plugin." + name + ".class");
        String directory = config.get(ConfigKeys.PLUGIN_DIRECTORY);

        Class clazz = null;
        File jarFile = new File(directory + "/" + pluginFile + ".jar");
        log.info("Loading plugin: " + plugin + " ,path=" + directory + "/" + plugin + ".jar");
        try {
            URL[] urls = {new URL("jar:file:" + jarFile.getAbsoluteFile() + "!/")};
            ClassLoader cl = URLClassLoader.newInstance(urls, getClass().getClassLoader());
            clazz = cl.loadClass(pluginMainClass);
        } catch (MalformedURLException | ClassNotFoundException e) {
            log.error("Unable to load plugin. plugin=" + plugin, e);
        }
        return clazz;
    }
}
