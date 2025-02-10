package org.nba.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerManager{

    private static Logger logger = null;

    public static Logger getLogger() {
        if (logger == null) {
            throw new IllegalStateException("Logger not initialized. Initialize logger using initLogger first.");
        }
        return logger;
    }

    public static void defaultLogger(){
        if (logger == null) {
            logger = LogManager.getLogger();
        }
    }
    public static void initLogger(String scenarioName) {
        if (logger == null) {
            String fileName = "logs/" + scenarioName
                    .replace(" ", "_")
                    .replace(",", "_")
                    .replace(";", "_") +".log";
            System.setProperty("logFilename", fileName);
            logger = LogManager.getLogger(scenarioName);

        }
    }

    public static void resetLogger() {
        logger = null;
        LogManager.shutdown();
    }
}
