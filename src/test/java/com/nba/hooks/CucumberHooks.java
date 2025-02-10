package com.nba.hooks;

import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nba.config.ConfigurationManager;
import org.nba.config.LoggerManager;
import org.nba.utils.ScreenshotHelper;
import org.nba.utils.WebDriverHelper;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterSuite;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.nba.config.ConfigurationManager.getConfig;
import static org.nba.config.LoggerManager.getLogger;
import static org.nba.utils.WebDriverHelper.getWebDriver;
import static org.nba.utils.WebDriverHelper.tearDown;

public class CucumberHooks{

    private static WebDriver driver;
    private static Logger logger;
    private static String scenarioName;
    private static String TEXT_FILE_TO_ATTACH = "jackets_details.txt";
    private static String CSV_FILE_TO_ATTACH = "footer_links.csv";

    @BeforeAll
    public static void beforeAllScenario(){
        ConfigurationManager.initConfig("src\\main\\resources\\config.properties");
        LoggerManager.defaultLogger();
        WebDriverHelper.getInstance();
    }
    @Before
    public void beforeScenario(Scenario scenario) {
        LoggerManager.initLogger(scenario.getName());
        logger = getLogger();
        scenarioName = scenario.getName().replace(" ", "_");
        logger.info(String.format("Starting Scenario: %s", scenarioName));
    }

    @After(order=1)
    public void afterScenario(Scenario scenario) {
        logger.info(String.format("Scenario Completed: %s", scenario.getStatus()));
        if (scenario.isFailed()) {
            logger.error(String.format("Scenario Failed: %s", scenario.getName()));
            logger.error("Scenario failed, taking screenshot...");
            try {
                final byte[] screenshot = ((TakesScreenshot) getWebDriver()).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "Screenshot on Failure");

            } catch (Exception e) {
                logger.error("Failed to capture screenshot on failure.", e);
            }
        } else {
            logger.info(String.format("Scenario Passed: %s", scenario.getName()));
            generateReport();

        }

    }

    @After("@attach_text_report")
    public void attachFileToReport(Scenario scenario){
        try{
            byte[] fileContent = Files.readAllBytes(Paths.get(TEXT_FILE_TO_ATTACH));
            scenario.attach(fileContent, "text/plain", TEXT_FILE_TO_ATTACH);
        }catch (IOException e){
            getLogger().error("Exception occurred while attaching file to report");
        }
    }

    @After("@attach_csv_report")
    public void attachCsvFileToReport(Scenario scenario){
        try{
            byte[] fileContent = Files.readAllBytes(Paths.get(CSV_FILE_TO_ATTACH));
            scenario.attach(fileContent, "text/plain", CSV_FILE_TO_ATTACH);
        }catch (IOException e){
            getLogger().error("Exception occurred while attaching file to report");
        }
    }

    @AfterAll
    public static void afterAllScenario(){
        tearDown();
    }

    public static void generateReport() {
        File reportOutputDirectory = new File("target/cucumber-reports/advanced-reports");
        List<String> jsonFiles = new ArrayList<>();
        jsonFiles.add("target/cucumber-reports/cucumber.json");
        String projectName = "NBACucumberProject";

        Configuration configuration = new Configuration(reportOutputDirectory, projectName);
        configuration.addClassifications("Platform", System.getProperty("os.name"));
        configuration.addClassifications("Browser", getConfig().getProperty("browser"));
        configuration.addClassifications("Scenario", scenarioName);
        ReportBuilder reportBuilder = new ReportBuilder(jsonFiles, configuration);
        reportBuilder.generateReports();
    }
}
