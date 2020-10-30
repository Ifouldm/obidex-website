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

@Slf4j
@Service
public class BookmarkService {
    @Autowired
    private BookmarkRepository repository;

    @Autowired
    private ScreenshotService screenshotService;

    public void save(Bookmark bookmark) {
        // If screenshot does not exist, fetch one.
        if (bookmark.getImage() == null) {
            String imageloc = screenshotService.getScreenshot(bookmark.getUrl(), bookmark.getName());
            bookmark.setImage(imageloc);
            log.info("Screenshot taken: {}", imageloc);
        }
        repository.save(bookmark);
    }

    public Page<Bookmark> findAll(int pageNo, int size) {
        Pageable bookmarksByName = PageRequest.of(pageNo - 1, size, Sort.by("name"));
        return repository.findAll(bookmarksByName);
    }

    public void deleteById(String id) {
        Optional<Bookmark> bookmark = repository.findById(id);
        if (bookmark.isPresent()) {
            repository.delete(bookmark.get());
        } else {
            log.error("Unable to find bookmark with id: ", id);
        }
    }

    public Bookmark findById(String id) {
        return repository.findById(id).orElse(new Bookmark());
    }

    public Object getBookmarkLoc() {
        return screenshotService.getLocation();
    }
}
