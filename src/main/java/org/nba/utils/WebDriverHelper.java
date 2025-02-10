package org.nba.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.nba.config.LoggerManager.getLogger;

public class WebDriverHelper {

    private static WebDriverHelper instance = null;
    private static WebDriver driver;
    private static WebDriverWait wait;

    private WebDriverHelper() {
        // Private constructor so this class cannot be instantiated directly.
        this.driver = DriverFactory.createDriver();
        this.wait = DriverFactory.getWebDriverWait();
    }

    public static WebDriverHelper getInstance() {
        if (instance == null) {
            instance = new WebDriverHelper();
        }
        return instance;
    }

    public static WebDriver getWebDriver() {
        return driver;
    }

    public static WebDriverWait waitUntil(){
        return wait;
    }

    public static void tearDown() {
        if (driver != null) {
            driver.quit();
            getLogger().info("*******Webdriver ShutDown*******");
            driver = null; // Ensure the WebDriver is fully garbage collected
            instance = null; // Help garbage collection for the singleton instance
            getLogger().info("*******Execution completed browser closed*******");
        }
    }
}
