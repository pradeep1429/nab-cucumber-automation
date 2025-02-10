package org.nba.utils;

import com.google.common.base.Stopwatch;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.core.util.SystemClock;
import org.openqa.selenium.JavascriptExecutor;

import java.util.StringJoiner;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import static java.lang.String.format;
import static java.lang.System.lineSeparator;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.apache.commons.lang3.BooleanUtils.toBoolean;
import static org.nba.config.LoggerManager.getLogger;
import static org.nba.utils.WebDriverHelper.getWebDriver;

public class WaitUtil {

    private static final String COMPLETED_DOCUMENT_STATE = "complete";
    private static final long WAIT_TIMEOUT = 30000L;
    private static final long WAIT_INTERVAL = 500L;
    private static final String NEW_LINE = System.lineSeparator();
    private static final String ASYNC_REQUESTS_FINISHED = new StringJoiner(lineSeparator())
            .add("return (")
            .add("(typeof jQuery !== 'undefined') ")
            .add("&& (jQuery.active <= 0) ")
            .add("&& (document.readyState == 'complete')")
            .add(");").toString();


    public static void waitABit(final long timeInMilliseconds)  {
        try {
            Thread.sleep(timeInMilliseconds);
        } catch (InterruptedException e) {
            getLogger().error(format("Wait interrupted: %s",e.getMessage()));
            throw new RuntimeException("System timer interrupted", e);
        }
    }

    public static void waitSilently(final BooleanSupplier suppliedCondition, final long waitTimeout,
                                    final long waitInterval) {
        final Stopwatch waitTimer = Stopwatch.createStarted();
        for (long secs = 0; secs <= waitTimeout; secs += waitInterval) {
            waitABit(waitInterval);
            if (suppliedCondition.getAsBoolean() || (waitTimer.elapsed(MILLISECONDS) > waitTimeout)) {
                break;
            }
        }
    }

    public static void waitForAsyncExecution() {
        waitSilently(
                () -> toBoolean(((JavascriptExecutor) getWebDriver()).executeScript(ASYNC_REQUESTS_FINISHED).toString()),
                WAIT_TIMEOUT, WAIT_INTERVAL);
    }

    public static void waitToBeFulfilled(final BooleanSupplier suppliedCondition, final long waitTimeout,
                                    final long waitInterval, final Supplier<RuntimeException> exceptionSupplier) {
        if (!suppliedCondition.getAsBoolean()) {
            String logMessage =
                    "Waiting for condition has began" + NEW_LINE +
                            format("Total wait timeout: %s ms.", waitTimeout) + NEW_LINE +
                            format("Wait interval: %s ms.", waitInterval);
            getLogger().info(logMessage);
            performWait(suppliedCondition, waitTimeout, waitInterval, exceptionSupplier);
        }
    }


    private static void performWait(final BooleanSupplier suppliedCondition, final long waitTimeout,
                                    final long waitInterval, final Supplier<RuntimeException> exceptionSupplier) {
        boolean isConditionMet = false;
        final Stopwatch waitTimer = Stopwatch.createStarted();
        for (long secs = 0; secs <= waitTimeout; secs += waitInterval) {
            waitABit(waitInterval);
            isConditionMet = suppliedCondition.getAsBoolean();
            if (isConditionMet) {
                getLogger().info("Condition has been met {}", NEW_LINE);
                break;
            }
            if ((waitTimer.elapsed(MILLISECONDS) > waitTimeout)) {
                getLogger().info("Time is up!{}", NEW_LINE);
                break;
            } else {
                debugElapsedTime(waitTimer.elapsed(MILLISECONDS));
            }
        }
        if (!isConditionMet) {
            throw exceptionSupplier.get();
        }
    }

    public static void waitForPageIsLoaded() {
        final BooleanSupplier pageLoadedComplete = () -> isPageInState(COMPLETED_DOCUMENT_STATE).getAsBoolean();
        final Supplier<RuntimeException> error = () -> new IllegalStateException("Timeout expired - wait condition was not fulfilled.");
        waitToBeFulfilled(pageLoadedComplete, 30000, 1000, error);
    }

    private static BooleanSupplier isPageInState(final String state) {
//        getLogger().info("@@@@@",((JavascriptExecutor) getWebDriver())
//                .executeScript("return document.readyState").toString());
        return () -> StringUtils.equals(((JavascriptExecutor) getWebDriver())
                .executeScript("return document.readyState").toString(), state);
    }

    private static void debugElapsedTime(final long millis) {
        final long seconds = (millis / 1000);
        getLogger().debug("{} second(s) waiting...", seconds);
    }
}
