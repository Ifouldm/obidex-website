package com.obidex.webserver.controller;

import com.obidex.webserver.model.Bookmark;
import com.obidex.webserver.service.BookmarkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/bookmarks")
public class BookmarkController {
    private static final String BOOKMARK_PAGE = "bookmarks";
    private static final String ADD_BOOKMARK_PAGE = "addBookmark";
    private static final String BOOKMARK = "bookmark";
    private static final String BOOKMARKS = "bookmarks";
    @Autowired
    BookmarkService bookmarkService;

    @PostMapping("/add")
    public String addBookmark(@ModelAttribute Bookmark bookmark, final BindingResult bindingResult, final ModelMap model) {
        if (bindingResult.hasErrors()) {
            log.error(bindingResult.toString());
            return BOOKMARK_PAGE;
        }
        log.trace("Add bookmarks page reached, bookmark to add: {}", bookmark);
        bookmarkService.save(bookmark);
        return "redirect:/" + BOOKMARK_PAGE;
    }

    @GetMapping("/add")
    public String newBookmark(Model model) {
        model.addAttribute(new Bookmark());
        return ADD_BOOKMARK_PAGE;
    }

    @GetMapping("/add/{id}")
    public String editBookmark(@PathVariable String id, Model model) {
        Bookmark bookmark = bookmarkService.findById(id);
        model.addAttribute(bookmark);
        return ADD_BOOKMARK_PAGE;
    }

    @GetMapping({"/", ""})
    public String getBookmarks(Model model, @RequestParam(defaultValue = "1") int page) {
        log.trace("Bookmarks reached");
        model.addAttribute(BOOKMARKS, bookmarkService.findAll(page, 8));
        model.addAttribute(BOOKMARK, new Bookmark());
        model.addAttribute("bookmarkLoc", bookmarkService.getBookmarkLoc());
        return BOOKMARK_PAGE;
    }

    @GetMapping("/delete/{id}")
    public String deleteBookmark(@PathVariable String id) {
        log.trace("Delete bookmark: {}", id);
        bookmarkService.deleteById(id);
        return "redirect:/" + BOOKMARK_PAGE;
    }
}
