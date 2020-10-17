package com.obidex.webserver.controller;

import com.obidex.webserver.model.Console;
import com.obidex.webserver.model.ConsoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/console")
public class ConsoleController {
    @Autowired
    private ConsoleRepository repository;

    @GetMapping("")
    public String getConsoles(Model model) {
        List<Console> consoles = repository.findAll();
        model.addAttribute("consoles", consoles);
        log.info("Get consoles reached {}", consoles);
        return "consoles";
    }

    @GetMapping("/{id}")
    public String getConsole(@PathVariable("id") String id, Model model) {
        Optional<Console> console = repository.findByConsoleId(id);
        log.info("Get console reached {}", id);
        if (console.isPresent()) {
            log.info("Console found {}", console.get());
            model.addAttribute("console", console.get());
            return "console";
        } else {
            log.error("Console not found");
            return "error";
        }
    }
}
