package com.nba.stepdef;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.nba.pages.SixersSteps;

import java.time.Duration;
import java.util.List;

import static java.lang.String.format;
import static org.nba.utils.WebDriverHelper.getWebDriver;
import static org.testng.Assert.assertListContains;
import static org.testng.Assert.assertTrue;

public class SixersScenario {
    private SixersSteps sixersSteps = new SixersSteps(getWebDriver());
    private static final String HOME_PAGE_TITLE = "Sixers - The official site of the NBA";

    @Given("user on the Sixers DP Home page")
    public void userOnTheSixersHomePage(){
        sixersSteps.openSixersPage();
        assertTrue(sixersSteps.getTitle().contains(HOME_PAGE_TITLE),
                format("Incorrect page title. %s", sixersSteps.getTitle()));
    }

    @When("user scroll down tile stories panel to visible")
    public void scrollDownABit(){
        sixersSteps.scrollTileStoriesToVisible();
    }
    @Then("tile hero stories contains {string}")
    public void userGetAvailableTileTitles(final String expectedTitle) {
        List<String> actualTitles = sixersSteps.getTileStoryTitles();
        assertTrue(actualTitles.contains(expectedTitle),
                format("%s is not present in %s", expectedTitle, actualTitles));
    }

    @Then("duration of playing {string} should be {int} seconds")
    public void durationOfPlayingShouldBe(final String title, final int time) {
        sixersSteps.verifyDuraionOfTileStoryPlayed(title, Duration.ofSeconds(time));
    }
}
