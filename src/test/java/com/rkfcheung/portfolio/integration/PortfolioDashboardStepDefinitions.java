package com.rkfcheung.portfolio.integration;

import com.rkfcheung.portfolio.connection.NavPricer;
import com.rkfcheung.portfolio.model.NavEntry;
import com.rkfcheung.portfolio.model.Position;
import com.rkfcheung.portfolio.service.RealTimeDashboard;
import com.rkfcheung.portfolio.source.Source;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
public class PortfolioDashboardStepDefinitions extends CucumberSpringConfiguration {
    private final NavPricer navPricer;
    private final RealTimeDashboard realTimeDashboard;
    private final Source source;

    @Given("the portfolio dashboard is connected to the Quote service")
    public void connectToQuoteService() {
        assertTrue(realTimeDashboard.isConnected());
    }

    @Given("the user has provided a CSV input file with portfolio positions")
    public void provideCSVInputFile() {
        assertTrue(source.available());
    }

    @When("the dashboard retrieves the portfolio data")
    public void retrievePortfolioData() {
        final List<Position> positions = source.load();
        assertEquals(positions.size(), navPricer.load().size());
        positions.forEach(it -> {
            final NavEntry entry = navPricer.get(it.getSecurity());
            assertNotNull(entry);
            assertEquals(0, it.getQty().compareTo(entry.getQty()));
        });
    }

    @Then("the user should see the portfolio summary with market values")
    public void displayPortfolioSummary() {
        final String summary = realTimeDashboard.summary();
        assertTrue(summary.contains("Total portfolio"));
    }

    @When("the dashboard receives an updated quotes")
    public void receiveUpdatedQuotes() {
        assertTrue(realTimeDashboard.updatedCount() > 1);
    }

    @Then("the user should see the updated portfolio summary with market values")
    public void displayUpdatedPortfolioSummary() throws InterruptedException {
        final long currentCount = realTimeDashboard.updatedCount();
        final String currentSummary = realTimeDashboard.summary();

        Thread.sleep(2_000L);
        if (realTimeDashboard.updatedCount() > currentCount) {
            assertNotEquals(currentSummary, realTimeDashboard.summary());
        } else {
            assertEquals(currentSummary, realTimeDashboard.summary());
        }
    }
}
