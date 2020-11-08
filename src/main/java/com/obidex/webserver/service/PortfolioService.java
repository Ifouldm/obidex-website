package com.obidex.webserver.service;

import com.obidex.webserver.model.Portfolio;
import com.obidex.webserver.model.PortfolioRepository;
import com.obidex.webserver.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * The Service class for retrieving, adding, updating, deleting Portfolio records
 */
@Service
public class PortfolioService {
    @Autowired
    PortfolioRepository portfolioRepository;
    @Autowired
    StorageService storageService;


    /**
     * Add a {@link Portfolio} to the collection
     *
     * @param portfolio The {@link Portfolio} to be stored in the database
     * @param files     Array of {@link MultipartFile} to be stored and associated with the Portfolio
     */
    public void addPortfolio(Portfolio portfolio, MultipartFile[] files) {
        if (portfolio.getImages() == null) {
            List<String> images = new ArrayList<>();
            for (MultipartFile file : files) {
                storageService.store(file);
                images.add(file.getOriginalFilename());
            }
            portfolio.setImages(images.toArray(new String[images.size()]));
        }
        portfolioRepository.save(portfolio);
    }

    /**
     * Returns all {@link Portfolio} documents as a list unsorted
     *
     * @return List of {@link Portfolio} documents
     */
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

    /**
     * Delete the specified {@link Portfolio} document
     *
     * @param id {@link Portfolio} id to be deleted
     */
    public void deleteById(String id) {
        portfolioRepository.deleteById(id);
    }

    /**
     * Returns the end point for the storage service location
     *
     * @return the end point for the storage service location
     */
    public String getImagePath() {
        return storageService.getFullPath();
    }
}
