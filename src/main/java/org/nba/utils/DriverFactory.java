package org.nba.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.Logger;
import org.nba.config.ConfigurationManager;
import org.nba.config.LoggerManager;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static org.nba.config.ConfigurationManager.getConfig;
import static org.nba.config.LoggerManager.getLogger;

public class DriverFactory {
    private static WebDriver driver;
    private static WebDriverWait wait;


    public static WebDriver createDriver() {
        getLogger().info("*********Initializing WebDriver**********");
        final String browser = getConfig().getProperty("browser");
        getLogger().info("********launching the '{}' browser********", browser);
        switch (browser.toLowerCase()) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
                break;
            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
                chromeOptions.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
//                chromeOptions.addArguments(Arrays.asList("--allow-running-insecure-content", "--ignore-certificate-errors",
//                        "--disable-popup-blocking", "--disable-dev-shm-usage", "--no-sandbox", "--disable-gpu",
//                        "--disable-blink-features=AutomationControlled", "--remote-allow-origins=*", "--disable-notifications"));
                chromeOptions.addArguments("--test-type");
                driver = new ChromeDriver(chromeOptions);
        }
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        return driver;
    }

    public static WebDriverWait getWebDriverWait(){
        if (wait == null) {
            getLogger().error("WebDriverWait is requested before WebDriver is instantiated.");
            throw new IllegalStateException("WebDriverWait is not instantiated yet!");
        }
        return wait;
    }
}
