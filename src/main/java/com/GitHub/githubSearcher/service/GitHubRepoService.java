package com.GitHub.githubSearcher.service;

import com.GitHub.githubSearcher.dto.GitHubSearchRequest;
import com.GitHub.githubSearcher.entity.RepositoryEntity;
import com.GitHub.githubSearcher.repository.GitHubRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for handling GitHub API requests and DB operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GitHubRepoService {

    private final GitHubRepository repositoryRepository;

    @Autowired
    private final RestTemplate restTemplate;

    /**
     * Searches GitHub using public REST API and stores results.
     */
    public List<RepositoryEntity> searchAndStore(GitHubSearchRequest request) {
        String url = String.format(
                "https://api.github.com/search/repositories?q=%s+language:%s&sort=%s&order=desc",
                request.getQuery(), request.getLanguage(), request.getSort()
        );

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        List<RepositoryEntity> savedRepos = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode items = root.path("items");

            for (JsonNode item : items) {
                RepositoryEntity repo = new RepositoryEntity();
                repo.setId(item.path("id").asLong());
                repo.setName(item.path("name").asText());
                repo.setDescription(item.path("description").asText(""));
                repo.setOwner(item.path("owner").path("login").asText());
                repo.setLanguage(item.path("language").asText());
                repo.setStars(item.path("stargazers_count").asInt());
                repo.setForks(item.path("forks_count").asInt());
                repo.setLastUpdated(LocalDateTime.parse(item.path("updated_at").asText().replace("Z", "")));

                // Update if exists, else save new
                repositoryRepository.save(repo);
                savedRepos.add(repo);
            }

            log.debug("Repositories fetched and saved successfully.");
        } catch (Exception e) {
            log.error("Failed to parse GitHub response", e);
        }

        return savedRepos;
    }

    /**
     * Filters stored repositories.
     */
    public List<RepositoryEntity> filterRepos(String language, Integer minStars, String sort) {
        List<RepositoryEntity> repos = repositoryRepository.findAll();

        if (language != null) {
            repos.removeIf(repo -> !repo.getLanguage().equalsIgnoreCase(language));
        }
        if (minStars != null) {
            repos.removeIf(repo -> repo.getStars() < minStars);
        }

        if (sort != null) {
            repos.sort((r1, r2) -> {
                return switch (sort) {
                    case "stars" -> r2.getStars() - r1.getStars();
                    case "forks" -> r2.getForks() - r1.getForks();
                    case "updated" -> r2.getLastUpdated().compareTo(r1.getLastUpdated());
                    default -> 0;
                };
            });
        }

        return repos;
    }


}