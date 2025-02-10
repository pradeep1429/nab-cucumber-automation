package com.nba.stepdef;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.nba.pages.WarriorShopSteps;
import org.nba.utils.WindowManager;

import static java.lang.String.format;
import static org.nba.utils.WebDriverHelper.getWebDriver;
import static org.testng.Assert.assertTrue;


public class WarriorsShopScenario {

    private WarriorShopSteps warriorShopSteps = new WarriorShopSteps(getWebDriver());
    private final static String HOME_PAGE_TITLE = "Golden State Warriors";
    private final static String NEWS_PAGE_TITLE = "News & Media";
    private final static String SHOP_PAGE_TITLE = "shop.warriors.com";


    @Given("user on the Warriors CP Home page")
    public void userOnTheWarriorsCpHomepage() {
        warriorShopSteps.openWarriorsPage();
        assertTrue(warriorShopSteps.getTitle().contains(HOME_PAGE_TITLE),
                format("Incorrect page title. %s", warriorShopSteps.getTitle()));
    }

    @When("user navigates to News & Media page")
    public void navigateToNewsMediaPage(){
        warriorShopSteps.openNewsMediaPage();
        assertTrue(warriorShopSteps.getTitle().contains(NEWS_PAGE_TITLE),
                format("Incorrect page title. %s", warriorShopSteps.getTitle()));
    }

    @When("user navigates to the {string} submenu from the {string} main menu")
    public void userNavigatesToTheSubmenuFromTheShopMainMenu(final String subMenuName, final String mainMenuName) {
        warriorShopSteps.selectItemFromMenu(mainMenuName, subMenuName);
        WindowManager.getWindowManager().switchToNewTab();
        assertTrue(warriorShopSteps.getTitle().contains(SHOP_PAGE_TITLE),
                format("Incorrect page title. %s", warriorShopSteps.getTitle()));
    }

    @When("user find all {string} products from all paginated pages")
    public void userFindAllProductsFromAllPaginatedPages(final String filterName) {
        warriorShopSteps.chooseDepartmentFilter(filterName);
        assertTrue(warriorShopSteps.getTitle().toLowerCase().contains(filterName.toLowerCase()), "Incorrect page title.");
    }

    @Then("user data scrape and store each Jacket's Price, Title, and Top Seller message to a text file")
    public void userStoreEachJacketPpriceTitleAndTopSellerMessageToTextfile() {
        warriorShopSteps.dataScrapeAndWriteResultsToTextFile("jackets_details.txt");
        WindowManager.getWindowManager().closeCurrentWindow();
    }

    @Then("user count the total number of video feeds greater than or equal to {int} days")
    public void userCountTotalNumberOfVideoFeeds(final int days) {
        warriorShopSteps.countTotalVideoFeedsGreaterThan(days);
    }

}
