package com.rkfcheung.portfolio.integration;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.fail;

public class PortfolioHealthCheckStepDefinitions {

    @Given("the Portfolio server is running")
    public void portfolioServerIsRunning() {
        fail("Step implementation not provided yet");
    }

    @When("the user performs a health check request")
    public void performHealthCheck() {
        fail("Step implementation not provided yet");
    }

    @Then("the user should receive a successful response")
    public void receiveSuccessfulResponse() {
        fail("Step implementation not provided yet");
    }

    @Then("the response should indicate that the server is healthy")
    public void responseIndicatesServerIsHealthy() {
        fail("Step implementation not provided yet");
    }
}
