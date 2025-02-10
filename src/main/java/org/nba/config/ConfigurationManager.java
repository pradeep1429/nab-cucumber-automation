package org.nba.config;

import exceptions.configruntimeexception;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import static org.nba.config.LoggerManager.getLogger;

public class ConfigurationManager {
    private String file;
    private Properties properties;
    private static ConfigurationManager config = null;

    public ConfigurationManager(String file) {
        this.file = file;
        properties = new Properties();
        loadProperties();
    }

    public static void initConfig(String path) {
        if (config == null) {
            config = new ConfigurationManager(path);
        }
    }

    public static ConfigurationManager getConfig() {
        if (config == null) {
            throw new IllegalStateException("Config file not initialized.");
        }
        return config;
    }

    private void loadProperties() {
        try (FileReader configReader = new FileReader(System.getProperty("user.dir")
                +System.getProperty("file.separator")
                +file)) {


            properties.load(configReader);
        } catch (IOException e) {
            getLogger().error("Exception while reading the property file {}", this.file);
            throw new configruntimeexception("exception while reading the property file");
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
