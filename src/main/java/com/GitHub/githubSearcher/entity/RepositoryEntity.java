package com.GitHub.githubSearcher.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity representing a GitHub repository.
 */
@Entity
@Table(name = "repositories")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepositoryEntity {

    @Id
    private Long id;

    private String name;

    @Column(length = 1000)
    private String description;

    private String owner;

    private String language;

    private Integer stars;

    private Integer forks;

    private LocalDateTime lastUpdated;
}
