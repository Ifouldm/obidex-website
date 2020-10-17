package com.obidex.webserver.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class RootController {

    @GetMapping("/")
    public String getIndex() {
        log.info("Root path reached");
        return "index";
    }
}
