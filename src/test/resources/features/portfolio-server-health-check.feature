Feature: Portfolio Server Health Check

  Scenario: Performing health check on the Portfolio server
    Given the Portfolio server is running
    When the user performs a health check request
    Then the user should receive a successful response
    And the response should indicate that the server is healthy
