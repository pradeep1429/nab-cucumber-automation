package org.nba.pages;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.nba.config.LoggerManager.getLogger;
import static org.nba.utils.DriverFactory.getWebDriverWait;
import static org.nba.utils.WaitUtil.waitForAsyncExecution;
import static org.nba.utils.WaitUtil.waitForPageIsLoaded;
import static org.nba.utils.WaitUtil.waitToBeFulfilled;

public class SixersSteps extends AbstractBasePage{

    private static WebDriver driver;
    private static final String SIXERS_PAGE_URL = "sixers";
    private static final String TILE_STORIES_FRAME = "//div[@role='tablist']";
    private static final String TILE_STORIES = "//button[contains(@class,'TileHeroStories')]";
    private static final String TILE_STORY = "//div[text()='%s']/parent::button";
    private static final String TILE_SELECTION_ATTRIBUTE = "aria-selected";

    public SixersSteps(WebDriver driver) {
        this.driver = driver;
    }

    public void openSixersPage() {
        openDispatcherPage(SIXERS_PAGE_URL);
        waitForPageIsLoaded();
    }

    public void scrollTileStoriesToVisible(){
        scrollVisibleElementToMiddle(TILE_STORIES_FRAME);
    }
    public List<String> getTileStoryTitles(){
        getWebDriverWait().until(ExpectedConditions.elementToBeClickable(findBy(TILE_STORIES_FRAME)));
        return findMultipleBy(TILE_STORIES).stream().map(WebElement::getText).collect(Collectors.toList());
    }

    public void verifyDuraionOfTileStoryPlayed(final String title, final Duration duration){
        WebElement tile = findBy(format(TILE_STORY,title));
        final Supplier<RuntimeException> error = () -> new IllegalStateException("Timeout expired - wait condition was not fulfilled.");
        final BooleanSupplier tileSelected = ()-> Boolean.valueOf(tile.getAttribute(TILE_SELECTION_ATTRIBUTE));
        final BooleanSupplier tileNotSelected = ()-> !Boolean.valueOf(tile.getAttribute(TILE_SELECTION_ATTRIBUTE));
        waitToBeFulfilled(tileSelected,80000,100, error);
        waitToBeFulfilled(tileNotSelected,duration.toMillis(),100, error);
    }

}
