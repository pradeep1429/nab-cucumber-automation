package org.nba.utils;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

import static java.lang.String.format;
import static java.util.Objects.isNull;
import static org.nba.config.LoggerManager.getLogger;
import static org.nba.utils.WaitUtil.waitToBeFulfilled;
import static org.nba.utils.WebDriverHelper.getWebDriver;

public class WindowManager {

    private static WindowManager instance;
    private final Set<String> windowHandles;
    private static final Long NEW_WINDOW_WAIT_TIMEOUT = 20000L;
    private static final Long NEW_WINDOW_WAIT_INTERVAL = 500L;

    private WindowManager() {
        windowHandles = new HashSet<>();
    }

    public static WindowManager getWindowManager() {
        if (isNull(instance)) {
            instance = new WindowManager();
        }
        return instance;
    }

    public Set<String> getWindowHandles() {
        return getWebDriver().getWindowHandles();
    }


    public void switchToNewTab() {
        waitForNewTab();
        windowHandles.addAll(getWebDriver().getWindowHandles());
        windowHandles.remove(getWebDriver().getWindowHandle());
        getWebDriver().switchTo().window(windowHandles.iterator().next());
        getLogger().info("Switched to new tab with url:{}", getWebDriver().getCurrentUrl());
        getLogger().info("Switched to new tab with title:{}", getWebDriver().getTitle());
    }

    private void waitForNewTab() {
        final Supplier<RuntimeException> error = () -> new IllegalStateException("New browser window not opened!");
        waitToBeFulfilled(() -> !Objects.equals(windowHandles, getWindowHandles()),
                NEW_WINDOW_WAIT_TIMEOUT, NEW_WINDOW_WAIT_INTERVAL, error);
    }

    public void closeCurrentWindow() {
        windowHandles.clear();
        windowHandles.addAll(getWebDriver().getWindowHandles());
        windowHandles.remove(getWebDriver().getWindowHandle());
        if (!windowHandles.isEmpty()) {
            getWebDriver().close();
            getWebDriver().switchTo().window(windowHandles.iterator().next());
        }
    }
}
