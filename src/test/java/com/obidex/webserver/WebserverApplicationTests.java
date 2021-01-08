package com.obidex.webserver;

import com.obidex.webserver.controller.BookmarkController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WebserverApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private BookmarkController controller;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
        assertNotNull(controller);
        assertTrue(restTemplate.getForObject("http://localhost:" + port + "/", String.class).contains("obidex"));
    }

}
