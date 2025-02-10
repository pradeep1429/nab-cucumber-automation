package com.nba.stepdef;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.nba.pages.BullSteps;

import java.util.List;

import static java.lang.String.format;
import static org.nba.utils.WebDriverHelper.getWebDriver;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class BullsScenario {

    public List<String> hyperLinks;

    private BullSteps bullSteps = new BullSteps(getWebDriver());
    private static final String HOME_PAGE_TITLE = "Bulls - The official site of the NBA";

    @Given("user on the Bulls DP Home page")
    public void userOnTheBullsCpHomepage() {
        bullSteps.openBullsPage();
        assertTrue(bullSteps.getTitle().contains(HOME_PAGE_TITLE),
                format("Incorrect page title. %s", bullSteps.getTitle()));
    }


    @When("user extract all footer hyperlinks")
    public void userExtractAllFooterHyperlinks() {
        hyperLinks = bullSteps.getFooterLinks();
        assertNotNull(hyperLinks,"Unable to fetch footer links. Collection is empty");
    }

    @Then("user check for duplicates and write all hyperlinks in a CSV file")
    public void userShouldSeeAllHyperlinksInACSVFile() {
        bullSteps.verifyDuplicateHyperLinksPresent(hyperLinks);
        bullSteps.writeLinksToCsvFile(hyperLinks);
    }

}
