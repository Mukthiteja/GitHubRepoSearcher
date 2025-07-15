package com.GitHub.githubSearcher.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class RepositoryEntityTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        RepositoryEntity repo = new RepositoryEntity();
        repo.setId(1L);
        repo.setName("Test Repo");
        repo.setDescription("Test description");
        repo.setOwner("TestOwner");
        repo.setLanguage("Java");
        repo.setStars(100);
        repo.setForks(10);
        repo.setLastUpdated(LocalDateTime.now());

        assertEquals(1L, repo.getId());
        assertEquals("Test Repo", repo.getName());
        assertEquals("Test description", repo.getDescription());
        assertEquals("TestOwner", repo.getOwner());
        assertEquals("Java", repo.getLanguage());
        assertEquals(100, repo.getStars());
        assertEquals(10, repo.getForks());
        assertNotNull(repo.getLastUpdated());
    }

    @Test
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();

        RepositoryEntity repo = new RepositoryEntity(
                2L,
                "Another Repo",
                "Another description",
                "AnotherOwner",
                "Python",
                200,
                20,
                now
        );

        assertEquals(2L, repo.getId());
        assertEquals("Another Repo", repo.getName());
        assertEquals("Another description", repo.getDescription());
        assertEquals("AnotherOwner", repo.getOwner());
        assertEquals("Python", repo.getLanguage());
        assertEquals(200, repo.getStars());
        assertEquals(20, repo.getForks());
        assertEquals(now, repo.getLastUpdated());
    }

    @Test
    void testEqualsAndHashCode() {
        RepositoryEntity repo1 = new RepositoryEntity();
        repo1.setId(1L);
        repo1.setName("Repo");

        RepositoryEntity repo2 = new RepositoryEntity();
        repo2.setId(1L);
        repo2.setName("Repo");

        assertEquals(repo1, repo2);
        assertEquals(repo1.hashCode(), repo2.hashCode());
    }

    @Test
    void testToString() {
        RepositoryEntity repo = new RepositoryEntity();
        repo.setId(99L);
        repo.setName("TestRepo");

        String str = repo.toString();
        assertTrue(str.contains("TestRepo"));
        assertTrue(str.contains("99"));
    }
}
