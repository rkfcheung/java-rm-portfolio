# java-rm-portfolio

Real-time Portfolio Dashboard on Console in Java 8

## Quote Service

The [`QuoteService`](src/main/java/com/rkfcheung/portfolio/service/QuoteService.java) is responsible for providing
updated quoted prices for portfolio positions. It acts as a central service that retrieves real-time market data and
calculates the current prices for various securities held in the portfolio. This service ensures that portfolio
positions are accurately valued based on the latest market information.

## Real-Time Dashboard

The [`RealTimeDashboard`](src/main/java/com/rkfcheung/portfolio/service/RealTimeDashboard.java) is a component that
subscribes to the `QuoteUpdate`s emitted by the `QuoteService`. Its primary function is to update the portfolio summary
in real-time based on the received updates. By continuously monitoring changes in quoted prices, the `RealTimeDashboard`
ensures that the portfolio summary displayed to users reflects the most current valuation of their investments.

## Build

```shell
git clone https://github.com/rkfcheung/java-rm-portfolio.git
cd java-rm-portfolio
./gradlew build
```

### Usage

```shell
cd java-rm-portfolio
./run.sh # Use the sample CSV: src/main/resources/sample-input.csv

./run.sh <another-input.csv>
```

The CSV file must adhere to the following format:

```csv
symbol,positionSize
AAPL,1000
AAPL-OCT-2024-110-C,-20000
AAPL-OCT-2024-110-P,20000
TSLA,-500
TSLA-NOV-2024-400-C,10000
TSLA-DEC-2024-400-P,-10000
```

## Development Information

| Item             | Value         |
|------------------|---------------|
| Operating System | MX Linux      |
| OpenJDK Version  | 1.8.0_402-402 |
| Gradle Version   | 8.7           |
| Spring WebFlux   | 2.7.18        |
| Cucumber         | 7.16.1        |
