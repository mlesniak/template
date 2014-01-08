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

    public <T> T getPlugin(Class<T> iface) {
        String directory = Config.get().get(ConfigKeys.PLUGIN_DIRECTORY);

        PluginRegistry plugin = PluginRegistry.get(iface);


        log.info("Loading plugin: " + plugin + " ,path=" + directory + "/" + plugin + ".jar");

        File jarFile = new File(directory + "/" + plugin + ".jar");

        try {
            URL myJarFile = new URL("jar","", "file:"+jarFile.getAbsolutePath()+"!/");
            URLClassLoader cl = URLClassLoader.newInstance(new URL[] {myJarFile});
            Class clazz= cl.loadClass(plugin.getMainClass());


        } catch (MalformedURLException | ClassNotFoundException e) {
            log.error("Unable to load plugin. plugin=" + plugin, e);
        }


        return null;
    }


}
