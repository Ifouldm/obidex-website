package com.obidex.webserver.service;

import com.obidex.webserver.model.PortfolioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;


class PortfolioServiceTest {
    @Autowired
    PortfolioRepository pr;

    @Test
    void addPortfolio() {
        int x = 1;
        assertEquals(2, x + 1);
    }
}