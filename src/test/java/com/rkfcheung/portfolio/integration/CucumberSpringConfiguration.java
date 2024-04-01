package com.rkfcheung.portfolio.integration;

import io.cucumber.spring.CucumberContextConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import javax.annotation.PostConstruct;

@Slf4j
@SpringBootTest
@ContextConfiguration
@CucumberContextConfiguration
public class CucumberSpringConfiguration {

    @PostConstruct
    public void init() {
        log.debug("Testing ...");
    }
}
