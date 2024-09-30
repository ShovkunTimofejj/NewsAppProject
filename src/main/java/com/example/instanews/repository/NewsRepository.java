package com.example.instanews.repository;

import com.example.instanews.model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository // Indicates that this interface is a repository bean that interacts with the database
public interface NewsRepository extends JpaRepository<News, UUID> {

    // Finds all news articles published between the specified start and end times
    List<News> findAllByPublicationTimeBetween(LocalDateTime start, LocalDateTime end);

    // Deletes all news articles published before the specified time
    void deleteAllByPublicationTimeBefore(LocalDateTime time);
}