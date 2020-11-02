package com.obidex.webserver.service;

import com.obidex.webserver.model.Bookmark;
import com.obidex.webserver.model.BookmarkRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
class BookmarkServiceTest {

    @Autowired
    BookmarkRepository repos;

    @Test
    void save() {
        Bookmark bm = new Bookmark("1", "test", "test", "http://www.google.com", "decription");
        Bookmark res = repos.save(bm);
        assertEquals(bm, res);
    }

    @Test
    void findAll() {

    }

    @Test
    void deleteById() {
    }

    @Test
    void findById() {
    }

    @Test
    void getBookmarkLoc() {
    }
}