package org.nba.pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import static java.lang.String.format;
import static org.nba.utils.WebDriverHelper.getWebDriver;
import static org.nba.config.LoggerManager.getLogger;
import static org.nba.config.ConfigurationManager.getConfig;
import static org.nba.utils.WebDriverHelper.tearDown;

public abstract class AbstractBasePage extends AbstractElement{

    public String getTitle() {
        return getWebDriver().getTitle();
    }

    public void openDispatcherPage(String pageUrl) {
        String currentBaseSite = getConfig().getProperty("baseUrl");
        String url = currentBaseSite + pageUrl;
        getLogger().info("PageURL: {}",url);
        try {
            getWebDriver().get(url);
        } catch (WebDriverException | NullPointerException exception) {
            getLogger().info("This site cant be reached due to some error or deployment {}", exception.getMessage());
            exception.printStackTrace();
            tearDown();
        }
    }

    protected WebElement scrollVisibleElementToMiddle(final String xpathLocator) {
        String scrollElementIntoMiddle = "var viewPortHeight = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);"
                + "var elementTop = arguments[0].getBoundingClientRect().top;"
                + "window.scrollBy(0, elementTop-(viewPortHeight/2));";
        final WebElement element = findBy(xpathLocator);
        ((JavascriptExecutor) getWebDriver()).executeScript(scrollElementIntoMiddle, element);
        return element;
    }

    public static void scrollToBottom() {
        ((JavascriptExecutor) getWebDriver())
                .executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    public static void scrollDownABit(){
        ((JavascriptExecutor) getWebDriver())
                .executeScript("window.scrollBy(0,250)", "");
    }

}
