package com.GitHub.githubSearcher.service;

import com.GitHub.githubSearcher.dto.GitHubSearchRequest;
import com.GitHub.githubSearcher.entity.RepositoryEntity;
import com.GitHub.githubSearcher.repository.GitHubRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GitHubRepoServiceTest {

    @Mock
    private GitHubRepository gitHubRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GitHubRepoServiceImpl gitHubRepoService; // Concrete class for testing

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Sample concrete subclass for testing abstract class
    static class GitHubRepoServiceImpl extends GitHubRepoService {
        public GitHubRepoServiceImpl(GitHubRepository repo, RestTemplate restTemplate) {
            super(repo, restTemplate);
        }
    }

    @Test
    public void testSearchAndStore_validRequest_parsesAndSaves() {
        // Arrange
        GitHubSearchRequest request = new GitHubSearchRequest("spring", "java", "stars");

        String mockJson = """
            {
              "items": [
                {
                  "id": 1,
                  "name": "spring-project",
                  "description": "test project",
                  "owner": { "login": "user1" },
                  "language": "Java",
                  "stargazers_count": 100,
                  "forks_count": 10,
                  "updated_at": "2024-06-01T10:00:00"
                }
              ]
            }
        """;

        when(restTemplate.getForEntity(anyString(), eq(String.class)))
                .thenReturn(ResponseEntity.ok(mockJson));

        when(gitHubRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        List<RepositoryEntity> result = gitHubRepoService.searchAndStore(request);

        // Assert
        assertEquals(1, result.size());
        assertEquals("spring-project", result.get(0).getName());
        verify(gitHubRepository, times(1)).save(any());
    }

    @Test
    public void testFilterRepos_filtersByLanguageAndStars() {
        // Arrange
        RepositoryEntity repo1 = new RepositoryEntity(1L, "Repo1", "desc", "owner1", "Java", 100, 20, null);
        RepositoryEntity repo2 = new RepositoryEntity(2L, "Repo2", "desc", "owner2", "Python", 50, 5, null);
        when(gitHubRepository.findAll()).thenReturn(new ArrayList<>(List.of(repo1, repo2)));


        // Act
        List<RepositoryEntity> result = gitHubRepoService.filterRepos("Java", 60, "stars");

        // Assert
        assertEquals(1, result.size());
        assertEquals("Repo1", result.get(0).getName());
    }
}
