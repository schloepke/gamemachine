package io.gamemachine.core;

import io.gamemachine.config.AppConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PluginManager {
    public static final Logger logger = LoggerFactory.getLogger("plugins");

    public static Plugin get(String name) {
        try {
            Class<?> klass = Class.forName("plugins." + name + "Plugin");
            Plugin plugin = (Plugin) klass.newInstance();
            return plugin;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void startAll() {
        for (String pluginName : AppConfig.getPlugins()) {
            Plugin plugin = get(pluginName);
            if (plugin != null) {
                logger.warn("Starting plugin " + pluginName);
                plugin.start();
            }
        }
    }
}
