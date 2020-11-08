package com.obidex.webserver.service;

import com.obidex.webserver.model.Bookmark;
import com.obidex.webserver.model.BookmarkRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * The Service class for retrieving, adding, updating, deleting Bookmark records
 */
@Slf4j
@Service
public class BookmarkService {
    @Autowired
    private BookmarkRepository repository;

    @Autowired
    private ScreenshotService screenshotService;

    /**
     * Save the {@link Bookmark} document to the collection also responsible for providing the screenshot
     *
     * @param bookmark The {@link Bookmark} document to be stored
     */
    public void save(Bookmark bookmark) {
        // If screenshot does not exist, fetch one.
        if (bookmark.getImage() == null) {
            String imageloc = screenshotService.getScreenshot(bookmark.getUrl(), bookmark.getName());
            bookmark.setImage(imageloc);
            log.info("Screenshot taken: {}", imageloc);
        }
        repository.save(bookmark);
    }

    /**
     * Returns a list of {@link Bookmark} documents paginated and sorted by name alphabetically
     *
     * @param pageNo Page No to be retrieved
     * @param size   Number of documents per page
     * @return {@link Page} of {@link Bookmark} documents
     */
    public Page<Bookmark> findAll(int pageNo, int size) {
        Pageable bookmarksByName = PageRequest.of(pageNo - 1, size, Sort.by("name"));
        return repository.findAll(bookmarksByName);
    }

    /**
     * Delete the associated Bookmark document
     *
     * @param id {@link Bookmark} document to be deleted
     */
    public void deleteById(String id) {
        Optional<Bookmark> bookmark = repository.findById(id);
        if (bookmark.isPresent()) {
            repository.delete(bookmark.get());
        } else {
            log.error("Unable to find bookmark with id: {}", id);
        }
    }

    /**
     * Retrieve {@link Bookmark} document using the associated id
     *
     * @param id The id of the {@link Bookmark} to be retrieved
     * @return {@link Bookmark} document of the associated id
     */
    public Bookmark findById(String id) {
        return repository.findById(id).orElse(new Bookmark());
    }

    /**
     * Returns the end point for the screenshot service.
     *
     * @return
     */
    public String getBookmarkLoc() {
        return screenshotService.getLocation();
    }
}
