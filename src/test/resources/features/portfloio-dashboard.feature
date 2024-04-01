Feature: Portfolio Dashboard

  Scenario: Displaying portfolio details
    Given the portfolio dashboard is connected to the Quote service
    And the user has provided a CSV input file with portfolio positions
    When the dashboard retrieves the portfolio data
    Then the user should see the portfolio summary with market values

  Scenario: Updating portfolio in real-time
    Given the portfolio dashboard is connected to the Quote service
    And the user has provided a CSV input file with portfolio positions
    When the dashboard receives an updated quotes
    Then the user should see the updated portfolio summary with market values
