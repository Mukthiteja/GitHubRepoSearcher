package com.GitHub.githubSearcher.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO for search request from client.
 */
@Data
@AllArgsConstructor
public class GitHubSearchRequest {
    private String query;
    private String language;
    private String sort; // stars, forks, updated
}
