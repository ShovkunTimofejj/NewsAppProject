package com.example.instanews.model;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity // Indicates that this class is a JPA entity
@Table(name = "news") // Specifies the database table name for this entity
public class News {

    @Id // Marks this field as the primary key
    @GeneratedValue(generator = "UUID") // Specifies that the ID should be generated using a UUID strategy
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator") // Defines the UUID generator
    @Column(name = "id", columnDefinition = "BINARY(16)", updatable = false, nullable = false) // Maps the ID column in the database
    private UUID id; // Unique identifier for the news article

    private String headline; // Headline of the news article
    private String description; // Description of the news article

    @Column(name = "publication_time") // Maps the publication_time column in the database
    private LocalDateTime publicationTime; // Time when the news article was published

    // Default constructor
    public News() {
    }

    // Constructor with parameters for initializing a news article
    public News(String headline, String description, LocalDateTime publicationTime) {
        this.headline = headline;
        this.description = description;
        this.publicationTime = publicationTime;
    }

    // Getter for the ID
    public UUID getId() {
        return id;
    }

    // Setter for the ID
    public void setId(UUID id) {
        this.id = id;
    }

    // Getter for the headline
    public String getHeadline() {
        return headline;
    }

    // Setter for the headline
    public void setHeadline(String headline) {
        this.headline = headline;
    }

    // Getter for the description
    public String getDescription() {
        return description;
    }

    // Setter for the description
    public void setDescription(String description) {
        this.description = description;
    }

    // Getter for the publication time
    public LocalDateTime getPublicationTime() {
        return publicationTime;
    }

    // Setter for the publication time
    public void setPublicationTime(LocalDateTime publicationTime) {
        this.publicationTime = publicationTime;
    }
}
