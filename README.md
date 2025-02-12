---
# Test Automation Suite for NBA Web Application

This is the central repository for the Test Automation Suite of the NBA Web Application. This suite uses Selenium WebDriver with a BDD approach using the Cucumber framework for behavior-driven development. The test scripts are intended to automate end-to-end tests for validation of critical user flows.

## System Requirements:
- Java JDK 17
- Maven 3.9.9
- ChromeDriver matching the local Chrome version
- Cucumber 7.17
- TestNG 7.9.0


## MyTestAutomationFramework
```
│
├───src
│   ├───main
│   │   ├───java
│   │   │   ├───com
│   │   │   │   ├───nba
│   │   │   │   │   ├───config
│   │   │   │   │   │   └───ConfigurationManager.java
|   |   |   |   |   |   └───LoggerManager.java
│   │   │   │   │   ├───pages
│   │   │   │   │   │   ├───AbstractBasePage.java
│   │   │   │   │   │   ├───AbstractElement.java
│   │   │   │   │   │   └───WarriorShopSteps.java
│   │   │   │   │   │   └───BullSteps.java
│   │   │   │   │   │   └───SixersSteps.java
│   │   │   │   │   └───utils
│   │   │   │   │       ├───DriverFactory.java
│   │   │   │   │       └───WebDriverHelper.java
│   │   │   │   │       └───WaitUtil.java
│   │   │   │   │       └───WindowManager.java
│   │   └───resources
│   │       ├───config.properties
│   │       └───log4j.properties
│   └───test
│       ├───java
│       │   ├───com
│       │   │   ├───nba
│       │   │   │   ├───hooks
│       │   │   │   │   └───CucumberHooks.java
│       │   │   │   ├───runners
│       │   │   │   │   └───CucumberRunner.java
│       │   │   │   ├───stepdefinitions
│       │   │   │   │   ├───BullScenario.java
│       │   │   │   │   └───SixersScenario.java
│       │   │   │   │   └───WarriorsScenario.java
│       └───resources
│           ├───features
│           │   ├───nba.feature
│           └───testng.xml
├───pom.xml
└───README.md
```

## Setup Instructions:
1. **Install Java JDK**:
   - Download and install JDK from [Oracle JDK](https://www.oracle.com/java/technologies/javase-downloads.html).
   - Set JAVA_HOME environment variable.

2. **Install Maven**:
   - Follow the installation guide provided here: [Maven Installation](https://maven.apache.org/install.html).

3. **Install Chrome and ChromeDriver**:
   - Download and install the latest Google Chrome from [Google Chrome](https://www.google.com/chrome/).
   - Download ChromeDriver from [ChromeDriver](https://sites.google.com/a/chromium.org/chromedriver/) and set the PATH variable.

4. **Clone the repository**:
   ```bash
   git clone https://github.com/pradeep/nab-automation.git
   cd nab-automation
   ```

5. **Install dependencies**:
   ```bash
   mvn clean install
   ```

## Running Tests:
Run tests using the following command in the terminal:
```bash
mvn clean test
```

## Directory Structure:
- **`src/test/java`**: Contains all test scripts and step definitions.
- **`src/test/resources`**: Contains Cucumber feature files.
- **`src/main/java`**: Contains utility classes page classes and common methods used across tests.

## Configuration Files:
- **`config.properties`**: Stores environment URLs, browser settings, and other configurable parameters.
- **`pom.xml`**: Contains project and configuration management, including dependencies.

## Reporting:
- **Test Reports** are generated using Cucumber's built-in report generator and can be found in `target/cucumber-reports` after the execution of tests.

## UseCase flow of Framework
```
               +--------------------------------+
               |          Test Execution        |
               |        (CucumberRunner)        |
               +-+--------------------------+---+
                 |              |           |
+----------------v------------+  +----------v---------------------------+
|        Hooks Setup          |  |             Feature Files            |
|     (CucumberHooks)         |  |    (nba.feature)   (sixers.feature)  |
+-----------+-----------------+  +----+-------------+-------------------+
|                          |             |                   |
|                          |             |                   |
+-------------------v----+ |             |                   |
|  Config & Logger Setup |               |                   |
| (ConfigurationManager, +<--------------+                   |
|    LoggerManager)      |                                   |
+------------+-----------+                                   |
|            |                                               |
+------------v-----------------------------------+           |
|       WebDriver Setup                          |           |
|  (DriverFactory, WindowManager,                |           |
|    WebDriverHelper, WaitUtil)                  |           |
+-------+--------------+-------+--------+--------+           |
|       |              |                |                    |
+-------v----v---+  +--v-----+ +--------v--------+           |
| Abstract Page |  |  Utils  | | Abstract        |           |
| Implementation|  | Modules | | Element Impl    |           |
| (Page Modules)|  |         | | (Page Elements) |           |
| (Step Modules)|  |         | |                 |           |
+-------+-------+  +---+-----+ +-----------------+           |
|       |              |            |                        |
+-------v--------------v------------v--+ +--------------------v------------+
| Scenario Step Definitions            | |    Test Scenario Implementation |
| (Step Binding with Gherkin Steps)    | |    (e.g., BullScenario,         |
| (e.g., BullSteps, SixersSteps)       | |         SixersScenario)         |
+--------------------------------------+ +---------------------------------+
```
## UseCase Flow

1. **Test Execution**
- Entry point facilitated by CucumberRunner.

2. **Hooks Setup**
- CucumberHooks handle pre and post-conditions for the tests.

3. **Config & Logger Setup**
- ConfigurationManager & LoggerManager perform initial loading of configurations and logging setups.

4. **WebDriver Setup**
- DriverFactory & WindowManager initialize WebDriver along with utilities like WebDriverHelper and WaitUtil for managing WebDriver interactions.

5. **Abstract Page and Element Implementation**
- Base classes (AbstractBasePage and AbstractElement) define common methods and fields that are extended by actual page modules, and element implementations.

6. **Scenario Step Definitions**
- Actual code implementing actions and interactions defined by Gherkin steps as specified in the feature files.

7. **Utils Modules**
- Shared utility methods that step definitions or pages might use.

8. **Test Scenario Implementation**
- Concrete scenarios tied to feature files (like BullScenario, SixersScenario) are what get executed as per associated feature definitions.

9. **Feature Files**
- nba.feature and sixers.feature where the Gherkin syntax for scenarios is defined.



