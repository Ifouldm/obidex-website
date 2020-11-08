package com.obidex.webserver.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
public class RootController {

    @GetMapping("/")
    public String getIndex() {
        log.info("Root path reached");
        return "index";
    }

    @GetMapping("/login")
    public String getLogin(@RequestParam(value = "logout", required = false) String logout,
                           @RequestParam(value = "error", required = false) String error,
                           Model model) {
        log.info("Login path reached, logout= {}, error= {}", logout, error);
        if (logout != null) {
            model.addAttribute(logout);
        }
        if (error != null)
            model.addAttribute(error);
        return "login";
    }
}
