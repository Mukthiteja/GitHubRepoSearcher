package com.GitHub.githubSearcher.repository;


import com.GitHub.githubSearcher.entity.RepositoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for accessing GitHub repositories in DB.
 */
@Repository
public interface GitHubRepository extends JpaRepository<RepositoryEntity, Long> {

    List<RepositoryEntity> findByLanguageIgnoreCase(String language);

    List<RepositoryEntity> findByStarsGreaterThanEqual(Integer stars);
}
