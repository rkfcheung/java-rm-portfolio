Feature: Portfolio Dashboard

  Scenario: Displaying portfolio details
    Given the portfolio dashboard is connected to the Portfolio server
    And the user has provided a CSV input file with portfolio positions
    When the dashboard retrieves the portfolio data
    Then the user should see the portfolio summary with market values

  Scenario: Updating portfolio in real-time
    Given the portfolio dashboard is connected to the Portfolio server
    And the user has provided a CSV input file with portfolio positions
    When the dashboard receives an updated CSV input file
    Then the user should see the updated portfolio summary with market values

  Scenario: Handling errors gracefully
    Given the portfolio dashboard is connected to the Portfolio server
    And there is an error in retrieving portfolio data
    Then the user should see an error message on the console
    And the dashboard should continue to function normally for other users
