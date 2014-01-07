package com.mlesniak.template.plugin;

import com.mlesniak.template.config.Config;
import com.mlesniak.template.config.ConfigKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PluginService {
    private Logger log = LoggerFactory.getLogger(PluginService.class);
    private static PluginService INSTANCE;

    public static PluginService get() {
        if (INSTANCE == null) {
            INSTANCE = new PluginService();
        }

        return INSTANCE;
    }

    // Registrieren
    // Zugriff über Interface
    // Aus Datenbank laden

    public <T> T get(Class<T> iface) {
        // Look at directory.
        String directory = Config.get().get(ConfigKeys.PLUGIN_DIRECTORY);

        // Map
        // Classloader



        return null;
    }


}
