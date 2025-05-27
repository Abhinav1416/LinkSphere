package com.linkedin.backend.AllControllers;

import com.linkedin.backend.AllModels.User;
import com.linkedin.backend.AllServices.SearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/search")
public class SearchController {
    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/users")
    public List<User> searchUsers(@RequestParam String query) {
        return searchService.searchUsers(query);
    }
}