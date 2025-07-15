package com.GitHub.githubSearcher.controller;

import com.GitHub.githubSearcher.dto.GitHubSearchRequest;
import com.GitHub.githubSearcher.entity.RepositoryEntity;
import com.GitHub.githubSearcher.service.GitHubRepoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GitHubRepoController.class)
class GitHubRepoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GitHubRepoService gitHubRepoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSearchAndSave() throws Exception {
        GitHubSearchRequest request = new GitHubSearchRequest("spring", "Java", "stars");

        RepositoryEntity repo = new RepositoryEntity();
        repo.setId(1L);
        repo.setName("test-repo");
        repo.setDescription("Test repo");
        repo.setOwner("octocat");
        repo.setLanguage("Java");
        repo.setStars(100);
        repo.setForks(50);
        repo.setLastUpdated(LocalDateTime.now());

        Mockito.when(gitHubRepoService.searchAndStore(Mockito.any()))
                .thenReturn(List.of(repo));

        mockMvc.perform(post("/api/github/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("test-repo"))
                .andExpect(jsonPath("$[0].language").value("Java"))
                .andExpect(jsonPath("$[0].stars").value(100));
    }

    @Test
    void testGetFilteredRepos() throws Exception {
        RepositoryEntity repo = new RepositoryEntity();
        repo.setId(2L);
        repo.setName("filtered-repo");
        repo.setLanguage("Java");
        repo.setStars(200);
        repo.setForks(80);
        repo.setDescription("Filtered");
        repo.setOwner("tester");
        repo.setLastUpdated(LocalDateTime.now());

        Mockito.when(gitHubRepoService.filterRepos("Java", 100, "stars"))
                .thenReturn(List.of(repo));

        mockMvc.perform(get("/api/github/repositories")
                        .param("language", "Java")
                        .param("minStars", "100")
                        .param("sort", "stars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("filtered-repo"))
                .andExpect(jsonPath("$[0].stars").value(200));
    }
}
