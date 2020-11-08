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

    /**
     * @param bookmark      The bookmmark object to be added
     * @param bindingResult Result of binding success
     * @param model
     * @return Redirect to Bookmarks list
     */
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

    /**
     * Form to add a new Bookmark
     *
     * @param model The model to which a blank bookmark is added
     * @return The add bookmark form
     */
    @GetMapping("/add")
    public String newBookmark(Model model) {
        model.addAttribute(new Bookmark());
        return ADD_BOOKMARK_PAGE;
    }

    /**
     * Form to edit an existing Bookmark
     *
     * @param id    The id of the bookmark to be amended
     * @param model The model to which the bookmark will be added
     * @return the Add bookmark form
     */
    @GetMapping("/add/{id}")
    public String editBookmark(@PathVariable String id, Model model) {
        Bookmark bookmark = bookmarkService.findById(id);
        model.addAttribute(bookmark);
        return ADD_BOOKMARK_PAGE;
    }

    /**
     * The main bookmarks page containing all of the Bookmarks paginated and listed Alphabetically.
     *
     * @param model The model to which the list of bookmarks is added
     * @param page  The current page
     * @return The bookmarks list
     */
    @GetMapping({"/", ""})
    public String getBookmarks(Model model, @RequestParam(defaultValue = "1") int page) {
        log.trace("Bookmarks reached");
        model.addAttribute(BOOKMARKS, bookmarkService.findAll(page, 8));
        model.addAttribute(BOOKMARK, new Bookmark());
        model.addAttribute("bookmarkLoc", bookmarkService.getBookmarkLoc());
        return BOOKMARK_PAGE;
    }

    /**
     * Delete a given Bookmark
     *
     * @param id The id of the Bookmark to be deleted
     * @return Redirect to the Bookmark list
     */
    @GetMapping("/delete/{id}")
    public String deleteBookmark(@PathVariable String id) {
        log.trace("Delete bookmark: {}", id);
        bookmarkService.deleteById(id);
        return "redirect:/" + BOOKMARK_PAGE;
    }
}
