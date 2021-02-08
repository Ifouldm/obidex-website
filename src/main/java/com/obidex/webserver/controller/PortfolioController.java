package com.obidex.webserver.controller;

import com.obidex.webserver.model.Portfolio;
import com.obidex.webserver.service.PortfolioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


@Slf4j
@Controller
@RequestMapping("/portfolio")
public class PortfolioController {
    private static final String PORTFOLIO = "portfolio";
    private static final String PORTFOLIO_PAGE = "portfolio";
    private static final String ADD_PORTFOLIO_PAGE = "addPortfolio";
    private static final String BUCKET_LOC = "bucketLoc";

    @Autowired
    PortfolioService portfolioService;

    @GetMapping({"", "/"})
    public String portfolio(Model model, @RequestParam(required = false) Map<String, String> params) {

        model.addAttribute(BUCKET_LOC, portfolioService.getImagePath());
        model.addAttribute(PORTFOLIO, portfolioService.findAll(params));
        return PORTFOLIO_PAGE;
    }

    @GetMapping("/add")
    public String addPortfolioForm(Model model) {
        model.addAttribute(PORTFOLIO, new Portfolio());
        log.trace("Add portfolio reached");
        return ADD_PORTFOLIO_PAGE;
    }

    @GetMapping("/add/{id}")
    public String addPortfolioForm(Model model, @PathVariable String id) {
        Portfolio portfolio = portfolioService.findById(id);
        model.addAttribute(PORTFOLIO, portfolio);
        log.trace("Edit portfolio reached: {}", portfolio);
        return ADD_PORTFOLIO_PAGE;
    }

    @PostMapping("/add")
    public String addPortfolio(@ModelAttribute Portfolio portfolio, @RequestParam(value = "file", required = false) MultipartFile[] files) {
        if (files == null) {
            log.info("No files to upload");
            portfolioService.addPortfolio(portfolio);
        } else {
            if ((files.length == 1) && (files[0] == null || files[0].isEmpty()))
                files = new MultipartFile[0];
            log.info("Form data: {}", portfolio);
            log.info("Files to upload ({}): {}", files.length, files);
            portfolioService.addPortfolio(portfolio, files);
        }
        return "redirect:/" + PORTFOLIO_PAGE;
    }

    @GetMapping("/delete/{id}")
    public String deletePortfolioForm(Model model, @PathVariable String id) {
        portfolioService.deleteById(id);
        log.trace("Delete portfolio reached: {}", id);
        return "redirect:/" + PORTFOLIO_PAGE;
    }
}
