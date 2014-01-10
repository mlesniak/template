package com.mlesniak.template.plugin;

import com.mlesniak.template.config.Config;
import com.mlesniak.template.config.ConfigKeys;
import com.mlesniak.template.dao.PluginDao;
import com.mlesniak.template.model.PluginDO;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

public class PluginService {
    private Logger log = LoggerFactory.getLogger(PluginService.class);
    private static PluginService INSTANCE;
    private Config config = Config.get();

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

    public void storeJARinDatabase(Class plugin) {
        byte[] bytes = null;
        try {
            bytes  = FileUtils.readFileToByteArray(new File(getFilePath(plugin)));
        } catch (IOException e) {
            log.error("Unable to store JAR in database.", e);
            return;
        }

        PluginDO pluginDO = new PluginDO();
        pluginDO.setJar(bytes);
        pluginDO.setName(plugin.getSimpleName());
        pluginDO.setTimestamp(System.currentTimeMillis());
        log.debug("Writing plugin to database. plugin=" + getFilePath(plugin));
        PluginDao.get().write(pluginDO);
    }

    @SuppressWarnings("unchecked")
    public <T> T getPlugin(Class<T> iface) {
        Class clazz = null;
        if (cache.containsKey(iface)) {
            clazz = cache.get(iface);
        } else {
            clazz = loadClassFromDatabase(iface);
            if (clazz == null) {
                clazz = loadClassFromDirectory(iface);
            }
            cache.put(iface, clazz);
        }

        try {
            return (T) (clazz.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("Unable to load plugin. plugin=" + iface.getSimpleName(), e);
        }

        return null;
    }

    private <T> Class loadClassFromDatabase(Class<T> iface) {
        String name = iface.getSimpleName();
        log.debug("Loading plugin from database. plugin=" + name);
        PluginDO pluginDO = PluginDao.get().getByName(name);
        if (pluginDO == null) {
            return null;
        }

        ByteArrayClassLoader cl = new ByteArrayClassLoader(pluginDO.getJar());
        String pluginMainClass = config.get("plugin." + name + ".class");
        try {
            return cl.loadClass(pluginMainClass);
        } catch (ClassNotFoundException e) {
            log.error("Unable to load plugin. plugin=" + iface, e);
            return null;
        }
    }

    private String getFilePath(Class iface) {
        String name = iface.getSimpleName();
        String pluginFile = config.get("plugin." + name + ".file");
        String directory = config.get(ConfigKeys.PLUGIN_DIRECTORY);
        return directory + "/" + pluginFile + ".jar";
    }

    private Class loadClassFromDirectory(Class plugin) {
        String name = plugin.getSimpleName();
        String pluginMainClass = config.get("plugin." + name + ".class");

        Class clazz = null;
        File jarFile = new File(getFilePath(plugin));
        log.info("Loading plugin: " + plugin + " ,path=" + getFilePath(plugin));
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
