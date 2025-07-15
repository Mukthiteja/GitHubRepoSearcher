package com.GitHub.githubSearcher.service;

import com.GitHub.githubSearcher.dto.GitHubSearchRequest;
import com.GitHub.githubSearcher.entity.RepositoryEntity;
import com.GitHub.githubSearcher.repository.GitHubRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GitHubRepoServiceTest {

    @Mock
    private GitHubRepository gitHubRepository;

    @InjectMocks
    private GitHubRepoServiceImpl gitHubRepoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    static class GitHubRepoServiceImpl extends GitHubRepoService {
        public GitHubRepoServiceImpl(GitHubRepository repositoryRepository) {
            super(repositoryRepository);
        }

        @Override
        public ResponseEntity<String> callGitHubApi(String url) {
            return null; // Not used in this test
        }
    }

    @Test
    void filterRepos_shouldFilterByLanguageAndStarsAndSort() {
        RepositoryEntity repo1 = new RepositoryEntity(1L, "Repo1", "desc", "user1", "Java", 100, 50, LocalDateTime.now());
        RepositoryEntity repo2 = new RepositoryEntity(2L, "Repo2", "desc", "user2", "Python", 200, 30, LocalDateTime.now());
        RepositoryEntity repo3 = new RepositoryEntity(3L, "Repo3", "desc", "user3", "Java", 50, 70, LocalDateTime.now().minusDays(1));

        when(gitHubRepository.findAll()).thenReturn(new ArrayList<>(List.of(repo1, repo2, repo3)));

        List<RepositoryEntity> filtered = gitHubRepoService.filterRepos("Java", 60, "stars");

        assertEquals(1, filtered.size());
        assertEquals("Repo1", filtered.get(0).getName());
    }

    @Test
    void filterRepos_shouldReturnAllIfNoFilters() {
        RepositoryEntity repo1 = new RepositoryEntity(1L, "Repo1", "desc", "user1", "Go", 10, 1, LocalDateTime.now());

        when(gitHubRepository.findAll()).thenReturn(List.of(repo1));

        List<RepositoryEntity> all = gitHubRepoService.filterRepos(null, null, null);

        assertEquals(1, all.size());
        assertEquals("Repo1", all.get(0).getName());
    }



}
