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
        PluginRegistry plugin = PluginRegistry.get(iface);

        if (cache.containsKey(iface)) {
            clazz = cache.get(iface);
        } else {
            clazz = loadClass(plugin);
        }

        try {
            return (T) (clazz.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("Unable to load plugin. plugin=" + plugin, e);
        }

        return null;
    }

    private Class loadClass(PluginRegistry plugin) {
        Class clazz = null;

        String directory = Config.get().get(ConfigKeys.PLUGIN_DIRECTORY);
        File jarFile = new File(directory + "/" + plugin + ".jar");
        log.info("Loading plugin: " + plugin + " ,path=" + directory + "/" + plugin + ".jar");
        try {
            URL[] urls = {new URL("jar:file:" + jarFile.getAbsoluteFile() + "!/")};
            ClassLoader cl = URLClassLoader.newInstance(urls, getClass().getClassLoader());
            clazz = cl.loadClass(plugin.getMainClass());

        } catch (MalformedURLException | ClassNotFoundException e) {
            log.error("Unable to load plugin. plugin=" + plugin, e);
        }
        return clazz;
    }
}
