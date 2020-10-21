package com.obidex.webserver.service;

import com.obidex.webserver.model.Portfolio;
import com.obidex.webserver.model.PortfolioRepository;
import com.obidex.webserver.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class PortfolioService {
    @Autowired
    PortfolioRepository portfolioRepository;
    @Autowired
    StorageService storageService;

    public void addPortfolio(Portfolio portfolio, MultipartFile[] files) {
        if (portfolio.getImages() == null) {
            List<String> images = new ArrayList<>();
            for (MultipartFile file : files) {
                storageService.store(file);
                images.add("uploads/" + file.getOriginalFilename());
            }
            portfolio.setImages(images.toArray(new String[images.size()]));
        }
        portfolioRepository.save(portfolio);
    }

    public List<Portfolio> findAll() {
        return portfolioRepository.findAll();
    }

    /**
     * @param id Portfolio id
     * @return Portfolio object for associated ID or blank Portfolio if not present
     */
    public Portfolio findById(String id) {
        return portfolioRepository.findById(id).orElse(new Portfolio());
    }

    public void deleteById(String id) {
        portfolioRepository.deleteById(id);
    }
}
