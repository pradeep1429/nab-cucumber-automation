package org.nba.pages;

import dto.ProductDetails;
import exceptions.datascrapeexception;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.String.format;
import static org.nba.config.LoggerManager.getLogger;
import static org.nba.utils.DriverFactory.getWebDriverWait;
import static org.nba.utils.WaitUtil.waitForAsyncExecution;
import static org.nba.utils.WaitUtil.waitForPageIsLoaded;

public class WarriorShopSteps extends AbstractBasePage{

    private static WebDriver driver;
    private static final String NEXT_BUTTON_ATTRIBUTE_NAME = "aria-disabled";
    private static final String TIME_ATTRIBUTE_NAME = "datetime";
    private static final String WARRIORS_PAGE_URL = "warriors/";
    private static final String NEWS_PAGE_URL = "warriors/news/";
    private static final String DIALOG_CLOSE = "//div[@role='dialog']//div[text()='x']";
    private static final String MENU_ITEM = "//ul[@role='menubar']//span[text()='%s']";
    private static final String SUBMENU_ITEM = "//div[contains(@class,'headerMenu')]//li[@role='menuitem']//a[text()=\"%s\"]";
    private static final String JACKETS_LINK = "(//a[contains(@data-trk-id,'%s')])";
    private static final String NEXT_BUTTON = "//div[@class='product-grid-bottom-area']//li/a[@data-trk-id='next-page']";
    private static final String PRODUCT_CARDS = "//div[@class='product-card row']";
    private static final String PRODUCT_CARD_TITLE = ".//div[@class='product-card-title']";
    private static final String PRODUCT_CARD_PRICE = ".//span[@class='sr-only']";
    private static final String PRODUCT_TOP_SELLER_MESSAGE = ".//div[@class='product-vibrancy top-seller-vibrancy']";
    private static final String VIDEO_FEEDS = "//div[@data-testid='tile-article']";
    private static final String VIDEO_FEED_TIME = ".//time";
    private static final String TOPSELLER_JS_QUERY = "return document.querySelector(\"div.grid.grid-small-2-medium-3.row.small-up-2.medium-up-3 " +
            "> div:nth-child(%d) > div > div:nth-child(2) > div.product-vibrancy-container\")";
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd yyyy");

    public WarriorShopSteps(WebDriver driver) {
        this.driver = driver;
    }

    public void openWarriorsPage() {
        openDispatcherPage(WARRIORS_PAGE_URL);
        waitForPageIsLoaded();
        findBy(DIALOG_CLOSE).click();
    }

    public void openNewsMediaPage() {
        openDispatcherPage(NEWS_PAGE_URL);
        waitForPageIsLoaded();
    }

    public void selectItemFromMenu(final String menuName, final String subMenu){
        WebElement menu = findBy(format(MENU_ITEM, menuName));
        getWebDriverWait().until(d -> menu.isDisplayed());
        withAction().moveToElement(menu).perform();
        withAction().moveToElement(findBy(format(SUBMENU_ITEM, subMenu))).click().perform();
        waitForAsyncExecution();
    }

    public void chooseDepartmentFilter(final String filterName){
        scrollVisibleElementToMiddle(format(JACKETS_LINK, filterName));
        findBy(format(JACKETS_LINK, filterName)).click();
        waitForPageIsLoaded();
    }

    public void dataScrapeAndWriteResultsToTextFile(final String fileName){
        List<ProductDetails> productDetails = scrapeAllJackets();
        writeResultsToTextFile(productDetails, fileName);
    }

    private void writeResultsToTextFile(List<ProductDetails> productDetails, final String fileName) {
        try {
            FileUtils.writeLines(new File(fileName), StandardCharsets.UTF_8.name(), productDetails);
        } catch (IOException e) {
            getLogger().error("Exception occurred while writing jacket details to the file {} {}", fileName, e.getMessage());
        }
    }

    public List<ProductDetails> scrapeAllJackets() {
        List<ProductDetails> allJackets = new ArrayList<>();
        try {
            do {
                scrollToBottom();
                List<ProductDetails> currentPageJackets = getProductDetails();
                allJackets.addAll(currentPageJackets);
            } while (clickNextButton());
        } catch (Exception e) {
            e.printStackTrace();
            getLogger().error("Exception while data scrapping: {}", e.getMessage());
            throw new datascrapeexception("Exception while data scrapping");
        }
        return allJackets;
    }

    private boolean clickNextButton() {
        if (Boolean.TRUE.equals(Boolean.valueOf(findBy(NEXT_BUTTON).getAttribute(NEXT_BUTTON_ATTRIBUTE_NAME)))) {
            return false;
        }
        WebElement nextButton = findBy(NEXT_BUTTON);
        if (nextButton.isEnabled()) {
            nextButton.click();
            waitForPageIsLoaded();
        }
        return true;
    }

    public List<ProductDetails> getProductDetails() {

        List<WebElement> productCards = findMultipleBy(PRODUCT_CARDS);
        return IntStream.range(0, productCards.size())
                .mapToObj(index -> mapToProductDetail(productCards.get(index), index+1))
                .collect(Collectors.toList());
    }

    private ProductDetails mapToProductDetail(final WebElement card, final int index) {
        String title = getText(card, By.xpath(PRODUCT_CARD_TITLE));
        String price = getText(card, By.xpath(PRODUCT_CARD_PRICE));
        String topSeller = getTopSellerText(card, index);
        return new ProductDetails(title, price, topSeller);
    }

    private String getTopSellerText(final WebElement parent, final int index) {
        String jsQuery = format(TOPSELLER_JS_QUERY, index);
        if (((JavascriptExecutor) driver).executeScript(jsQuery) == null) {
            return "";
        }
        return getText(parent, By.xpath(WarriorShopSteps.PRODUCT_TOP_SELLER_MESSAGE));
    }

    public int countTotalVideoFeedsGreaterThan(int days){
        scrollToBottom();
        List<WebElement> videoFeeds = findMultipleBy(VIDEO_FEEDS);
        getLogger().info("Total number of video feeds: {}", videoFeeds.size());
        LocalDate daysAgo = LocalDate.now().minusDays(days);
        long countRecentVideos = videoFeeds.stream()
                .map(video -> safeFindElement(video, By.xpath(VIDEO_FEED_TIME)).getAttribute(TIME_ATTRIBUTE_NAME))
                .map(videoDateStr -> LocalDate.parse(videoDateStr, formatter))
                .filter(videoDate -> !videoDate.isBefore(daysAgo))
                .count();
        getLogger().info("Number of video feeds grater than or equal to {} days: {}",days,countRecentVideos);
        return (int) countRecentVideos;
    }


}
