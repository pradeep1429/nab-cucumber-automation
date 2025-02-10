package org.nba.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.nba.config.LoggerManager.getLogger;
import static org.nba.utils.WebDriverHelper.getWebDriver;

public class AbstractElement {

    public Actions withAction() {
        final Actions proxiedDriver = new Actions(getWebDriver());
        return proxiedDriver;
    }

    public WebElement findBy(final String xpathOrCssSelector) {
        return  getWebDriver().findElement(createByLocator(xpathOrCssSelector));
    }

    public List<WebElement> findMultipleBy(final String xpathOrCssSelector) {
        List<WebElement> matchingWebElements;
        try {
            matchingWebElements = getWebDriver().findElements(createByLocator(xpathOrCssSelector));
        } catch (Exception e) {
            matchingWebElements = new ArrayList<>();
        }
        return matchingWebElements;
    }

    public WebElement safeFindElement(WebElement parent, By locator) {
        try {
            return parent.findElement(locator);
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public boolean isElementPresent(final String xpathLocator) {
        final boolean isElementsPresent = isNotEmpty(findMultipleBy(xpathLocator));
        return isElementsPresent;
    }

    protected String getText(final String xpathLocator) {
        return isElementPresent(xpathLocator) ? findBy(xpathLocator).getText() : null;
    }

    public String getText(WebElement parent, By locator) {
        WebElement element = safeFindElement(parent, locator);
        return (element != null) ? element.getText().trim() : "";
    }

    public static By createByLocator(final String xpathOrCssSelector) {
        final By locator;
        if (isXPath(xpathOrCssSelector)) {
            locator = By.xpath(xpathOrCssSelector);
        } else {
            locator = By.cssSelector(xpathOrCssSelector);
        }
        return locator;
    }

    private static boolean isXPath(final String xpathExpression) {
        boolean isXpath = true;
        final XPath xpath = XPathFactory.newInstance().newXPath();
        try {
            xpath.compile(xpathExpression);
        } catch (final XPathExpressionException e) {
            getLogger().trace("Error while evaluating xpath", e);
            isXpath = false;
        }
        return isXpath;
    }
}
