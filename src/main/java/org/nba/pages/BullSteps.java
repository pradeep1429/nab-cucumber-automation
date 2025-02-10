package org.nba.pages;

import exceptions.filewritingruntimeexception;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import static org.nba.config.LoggerManager.getLogger;
import static org.nba.utils.WaitUtil.waitForPageIsLoaded;

public class BullSteps extends AbstractBasePage{

    private static WebDriver driver;
    private static final String CSV_FILE_NAME = "footer_links.csv";
    private static final String BULLS_PAGE_URL = "bulls";
    private static final String FOOTER_LINKS = "//li[@data-testid='footer-list-item']/a";


    public BullSteps(WebDriver driver) {
        this.driver = driver;
    }

    public void openBullsPage() {
        openDispatcherPage(BULLS_PAGE_URL);
        waitForPageIsLoaded();
    }

    public List<String> getFooterLinks(){
        scrollToBottom();
        List<WebElement> footerLinks = findMultipleBy(FOOTER_LINKS);
        return footerLinks.stream().map(element-> element.getAttribute("href")).collect(Collectors.toList());
    }

    public void writeLinksToCsvFile(List<String> hyperlinks){
        verifyDuplicateHyperLinksPresent(hyperlinks);
        try (FileWriter csvWriter = new FileWriter(CSV_FILE_NAME)) {
            csvWriter.append("Hyperlink\n");
            for (String link : hyperlinks) {
                csvWriter.append(link).append("\n");
            }
        } catch (IOException e) {
            getLogger().error("Exception occurred while writing to csv file: {} {}",CSV_FILE_NAME, e.getMessage());
            e.printStackTrace();
            throw new filewritingruntimeexception("Exception while writing to file");
        }
    }

    public void verifyDuplicateHyperLinksPresent(List<String> hyperlinks){
        Set<String> duplicates = new HashSet<>();
        for (String link : hyperlinks) {
            if (!duplicates.add(link)) {
                getLogger().info("Duplicate hyperlink found: {}", link);
            }
        }
        if (duplicates.isEmpty()) {
            getLogger().info("No duplicate hyperlinks found.");
        }
    }
}
