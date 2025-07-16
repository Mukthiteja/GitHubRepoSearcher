package com.GitHub.githubSearcher.controller;

import com.GitHub.githubSearcher.dto.GitHubSearchRequest;
import com.GitHub.githubSearcher.entity.RepositoryEntity;
import com.GitHub.githubSearcher.service.GitHubRepoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling GitHub repository APIs.
 */
@RestController
@RequestMapping("/api/github")
@RequiredArgsConstructor
@Slf4j
public class GitHubRepoController {

    @Autowired
    private final GitHubRepoService gitHubRepoService;

    /**
     * POST /api/github/search
     * Search GitHub repositories and save to DB.
     */
    @PostMapping("/search")
    public List<RepositoryEntity> searchAndSave(@RequestBody GitHubSearchRequest request) {
        log.debug("Search request received: {}", request);
        return gitHubRepoService.searchAndStore(request);
    }

    /**
     * GET /api/github/repositories
     * Filter stored repositories from DB.
     */
    @GetMapping("/repositories")
    public List<RepositoryEntity> getFilteredRepos(
            @RequestParam(required = false) String language,
            @RequestParam(required = false) Integer minStars,
            @RequestParam(defaultValue = "stars") String sort
    ) {
        log.debug("Filtering repositories - Language: {}, MinStars: {}, Sort: {}", language, minStars, sort);
        return gitHubRepoService.filterRepos(language, minStars, sort);
    }
}
