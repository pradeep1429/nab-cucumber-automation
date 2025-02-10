Feature: Sixers hero stories slider duration


  Background:
    Given user on the Sixers DP Home page


  Scenario Outline: Validate slide titles and durations
    When user scroll down tile stories panel to visible
    Then tile hero stories contains '<expectedTitle>'
    And duration of playing '<expectedTitle>' should be 10 seconds

    Examples:
      | expectedTitle                                      |
      | 76ers sign Justin Edwards to Standard NBA Contract |
      | 76ers Play Bucks Today On The Road                 |
      | Daryl Morey Press Conference Recap Video           |
      | Sixers Sign Chuma Okeke to 10-Day Contract         |
      | 76ers Complete Trade With Detroit Pistons          |