package com.GitHub.githubSearcher.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GitHubSearchRequestTest {

    @Test
    void testConstructorAndGetters() {
        // Given
        String query = "spring";
        String language = "Java";
        String sort = "stars";

        // When
        GitHubSearchRequest request = new GitHubSearchRequest(query, language, sort);

        // Then
        assertEquals("spring", request.getQuery());
        assertEquals("Java", request.getLanguage());
        assertEquals("stars", request.getSort());
    }

    @Test
    void testSettersAndToString() {
        // Given
        GitHubSearchRequest request = new GitHubSearchRequest("initial", "Python", "forks");

        // When
        request.setQuery("new-query");
        request.setLanguage("Go");
        request.setSort("updated");

        // Then
        assertEquals("new-query", request.getQuery());
        assertEquals("Go", request.getLanguage());
        assertEquals("updated", request.getSort());

        // Optional: check toString() contains values
        String toString = request.toString();
        assertTrue(toString.contains("new-query"));
        assertTrue(toString.contains("Go"));
        assertTrue(toString.contains("updated"));
    }

    @Test
    void testEquality() {
        GitHubSearchRequest req1 = new GitHubSearchRequest("q", "Java", "stars");
        GitHubSearchRequest req2 = new GitHubSearchRequest("q", "Java", "stars");

        assertEquals(req1, req2);
        assertEquals(req1.hashCode(), req2.hashCode());
    }
}
