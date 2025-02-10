package org.nba.utils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;

public class ScreenshotHelper {

    public static String takeScreenshot(WebDriver driver, String scenarioName) {
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String imageFileName = "screenshot-" + scenarioName.replace(" ", "_") + "-" + System.currentTimeMillis() + ".png";
        String filePath = "target/cucumber-reports/screenshots/" + imageFileName;
        try {
            FileUtils.copyFile(scrFile, new File(filePath));
            return filePath;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
