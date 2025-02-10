Feature: Warriors Shop Men's Jackets Listing

  @attach_text_report
  Scenario: Collect and store details of all Men's Jackets
    Given user on the Warriors CP Home page
    When user navigates to the "Men's" submenu from the 'Shop' main menu
    And user find all 'jackets' products from all paginated pages
    Then user data scrape and store each Jacket's Price, Title, and Top Seller message to a text file

  Scenario: Count video feeds from news and fixtures
    Given user on the Warriors CP Home page
    When user navigates to News & Media page
    Then user count the total number of video feeds greater than or equal to 3 days

    @attach_csv_report
  Scenario: Extract hyperlinks from footer and check for duplicates
    Given user on the Bulls DP Home page
    When user extract all footer hyperlinks
    Then user check for duplicates and write all hyperlinks in a CSV file
