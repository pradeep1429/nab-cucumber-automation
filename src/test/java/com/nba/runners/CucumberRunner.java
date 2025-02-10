package com.nba.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

@CucumberOptions(
        features = {"src/test/resources/features/"},
        plugin = { "pretty", "json:target/cucumber-reports/cucumber.json",
                "junit:target/cucumber-reports/cucumber.xml",
                "html:target/cucumber-reports/cucumber-reports.html"},
        glue = {"com.nba.stepdef","com.nba.hooks"}, monochrome = true)


public class CucumberRunner extends AbstractTestNGCucumberTests {
    @BeforeClass
    @Parameters("tags")
    public void setup(String tags) {
        // Set system property that Cucumber recognizes for tag expressions
        System.setProperty("cucumber.filter.tags", tags);
    }
}
