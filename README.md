# java-rm-portfolio

Real-time Portfolio Dashboard on Console in Java 8

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
