package com.GitHub.githubSearcher.service;

import com.GitHub.githubSearcher.dto.GitHubSearchRequest;
import com.GitHub.githubSearcher.entity.RepositoryEntity;
import com.GitHub.githubSearcher.repository.GitHubRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GitHubRepoServiceTest {

    private GitHubRepository mockRepo;

    private GitHubRepoService service;

    @BeforeEach
    void setup() {
        mockRepo = mock(GitHubRepository.class);

        service = new GitHubRepoService(mockRepo) {
            @Override
            public ResponseEntity<String> callGitHubApi(String url) {
                // Simulated GitHub JSON response (shortened for brevity)
                String mockJson = """
                        {
                          "items": [
                            {
                              "id": 12345,
                              "name": "mock-repo",
                              "description": "A mock repo",
                              "owner": { "login": "mock-owner" },
                              "language": "Java",
                              "stargazers_count": 100,
                              "forks_count": 20,
                              "updated_at": "2025-07-15T08:00:00"
                            }
                          ]
                        }
                        """;
                return ResponseEntity.ok(mockJson);
            }
        };
    }

    @Test
    void testSearchAndStore_success() {
        GitHubSearchRequest request = new GitHubSearchRequest("spring", "Java", "stars");

        ArgumentCaptor<RepositoryEntity> captor = ArgumentCaptor.forClass(RepositoryEntity.class);

        when(mockRepo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        List<RepositoryEntity> results = service.searchAndStore(request);

        verify(mockRepo, times(1)).save(captor.capture());

        RepositoryEntity saved = captor.getValue();

        assertEquals(1, results.size());
        assertEquals("mock-repo", saved.getName());
        assertEquals("mock-owner", saved.getOwner());
        assertEquals(100, saved.getStars());
        assertEquals("Java", saved.getLanguage());
    }

    @Test
    void testFilterRepos_byLanguageAndStars() {
        RepositoryEntity repo1 = new RepositoryEntity(1L, "Repo1", "Test1", "Alice", "Java", 150, 30, LocalDateTime.now());
        RepositoryEntity repo2 = new RepositoryEntity(2L, "Repo2", "Test2", "Bob", "Python", 50, 10, LocalDateTime.now());
        when(mockRepo.findAll()).thenReturn(List.of(repo1, repo2));

        List<RepositoryEntity> filtered = service.filterRepos("Java", 100, "stars");

        assertEquals(1, filtered.size());
        assertEquals("Repo1", filtered.get(0).getName());
    }

    @Test
    void testFilterRepos_sortedByForks() {
        RepositoryEntity r1 = new RepositoryEntity(1L, "RepoA", "Desc", "User", "Java", 100, 5, LocalDateTime.now());
        RepositoryEntity r2 = new RepositoryEntity(2L, "RepoB", "Desc", "User", "Java", 90, 15, LocalDateTime.now());

        when(mockRepo.findAll()).thenReturn(List.of(r1, r2));

        List<RepositoryEntity> sorted = service.filterRepos("Java", null, "forks");

        assertEquals("RepoB", sorted.get(0).getName());  // higher forks
    }
}
