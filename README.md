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

## Sample Output

```text
## 1 Market Data Update
TSLA change to 449.86
AAPL change to 109.93

## Portfolio
symbol                                      price              qty            value
AAPL                                       109.93         1,000.00       109,930.00
AAPL-OCT-2024-110-C                         19.02       -20,000.00      -380,400.00
AAPL-OCT-2024-110-P                         17.81        20,000.00       356,200.00
TSLA                                       449.86          -500.00      -224,930.00
TSLA-NOV-2024-400-C                        135.10        10,000.00     1,351,000.00
TSLA-DEC-2024-400-P                         86.08       -10,000.00      -860,800.00

#Total portfolio                                                         351,000.00


## 2 Market Data Update
TSLA change to 449.69

## Portfolio
symbol                                      price              qty            value
AAPL                                       109.93         1,000.00       109,930.00
AAPL-OCT-2024-110-C                         19.02       -20,000.00      -380,400.00
AAPL-OCT-2024-110-P                         17.81        20,000.00       356,200.00
TSLA                                       449.69          -500.00      -224,845.00
TSLA-NOV-2024-400-C                        134.98        10,000.00     1,349,800.00
TSLA-DEC-2024-400-P                         86.13       -10,000.00      -861,300.00

#Total portfolio                                                         349,385.00


## 3 Market Data Update
AAPL change to 109.94

## Portfolio
symbol                                      price              qty            value
AAPL                                       109.94         1,000.00       109,940.00
AAPL-OCT-2024-110-C                         19.02       -20,000.00      -380,400.00
AAPL-OCT-2024-110-P                         17.81        20,000.00       356,200.00
TSLA                                       449.69          -500.00      -224,845.00
TSLA-NOV-2024-400-C                        134.98        10,000.00     1,349,800.00
TSLA-DEC-2024-400-P                         86.13       -10,000.00      -861,300.00

#Total portfolio                                                         349,395.00
```

## Development Information

| Item             | Value         |
|------------------|---------------|
| Operating System | MX Linux      |
| OpenJDK Version  | 1.8.0_402-402 |
| Gradle Version   | 8.7           |
| Spring WebFlux   | 2.7.18        |
| Cucumber         | 7.16.1        |
