package com.mlesniak.template.plugin;

import com.mlesniak.template.config.Config;
import com.mlesniak.template.config.ConfigKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class PluginService {
    private Logger log = LoggerFactory.getLogger(PluginService.class);
    private static PluginService INSTANCE;

    public static PluginService get() {
        if (INSTANCE == null) {
            INSTANCE = new PluginService();
        }

        return INSTANCE;
    }

    @SuppressWarnings("unchecked")
    public <T> T getPlugin(Class<T> iface) {
        String directory = Config.get().get(ConfigKeys.PLUGIN_DIRECTORY);
        PluginRegistry plugin = PluginRegistry.get(iface);
        File jarFile = new File(directory + "/" + plugin + ".jar");
        log.info("Loading plugin: " + plugin + " ,path=" + directory + "/" + plugin + ".jar");

        try {
            URL[] urls = {new URL("jar:file:" + jarFile.getAbsoluteFile() + "!/")};
            ClassLoader cl = URLClassLoader.newInstance(urls, getClass().getClassLoader());
            Class clazz = cl.loadClass(plugin.getMainClass());
            return (T)(clazz.newInstance());
        } catch (MalformedURLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            log.error("Unable to load plugin. plugin=" + plugin, e);
        }

        return null;
    }


}
