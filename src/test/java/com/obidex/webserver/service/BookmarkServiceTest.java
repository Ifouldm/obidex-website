package com.obidex.webserver.service;

import com.obidex.webserver.model.Bookmark;
import com.obidex.webserver.model.BookmarkRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        Bookmark bm = new Bookmark("2", "test", "test", "http://www.google.com", "decription");
        repos.save(bm);
        List<Bookmark> entries = repos.findAll();
        assertTrue(entries.size() > 0);
        assertTrue(entries.contains(bm));
    }

    @Test
    void deleteById() {
        String id = "3";
        Bookmark bm = new Bookmark(id, "delete", "test", "http://www.google.com", "decription");
        repos.save(bm);
        assertTrue(repos.findAll().contains(bm));
        repos.deleteById(id);
        assertFalse(repos.findById(id).isPresent());
    }

    @Test
    void findById() {
        String validId = "4";
        String invalidId = "Incorrect";
        Bookmark bm = new Bookmark(validId, "test", "test", "http://www.google.com", "decription");
        repos.save(bm);
        assertTrue(repos.findById(validId).isPresent());
        assertFalse(repos.findById(invalidId).isPresent());
    }
}