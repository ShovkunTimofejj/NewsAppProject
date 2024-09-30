package com.example.instanews.service;

import com.example.instanews.model.News;
import com.example.instanews.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class NewsService {

    private static final Logger logger = Logger.getLogger(NewsService.class.getName());

    @Autowired
    private NewsRepository newsRepository;

    // Retrieves all news articles
    public List<News> getAllNews() {
        logger.info("Retrieving all news articles");
        return newsRepository.findAll();
    }

    // Retrieves news articles published within a specified time period
    public List<News> getNewsByTimePeriod(LocalDateTime start, LocalDateTime end) {
        logger.info("Retrieving news articles from " + start + " to " + end);
        return newsRepository.findAllByPublicationTimeBetween(start, end);
    }

    // Retrieves a news article by its ID
    public News getNewsById(UUID id) {
        logger.info("Retrieving news article with ID: " + id);
        return newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News article with ID " + id + " not found"));
    }

    // Filters news articles based on the time of day (morning, day, evening)
    public List<News> getNewsByTimeOfDay(String timePeriod) {
        LocalDateTime start;
        LocalDateTime end;

        switch (timePeriod.toLowerCase()) {
            case "morning":
                start = LocalDateTime.now().withHour(6).withMinute(0);
                end = LocalDateTime.now().withHour(12).withMinute(0);
                break;
            case "day":
                start = LocalDateTime.now().withHour(12).withMinute(0);
                end = LocalDateTime.now().withHour(18).withMinute(0);
                break;
            case "evening":
                start = LocalDateTime.now().withHour(18).withMinute(0);
                end = LocalDateTime.now().withHour(23).withMinute(59);
                break;
            default:
                throw new IllegalArgumentException("Invalid time period: " + timePeriod);
        }
        logger.info("Filtering news articles by time of day: " + timePeriod);
        return newsRepository.findAllByPublicationTimeBetween(start, end);
    }

    // Saves a news article and deletes old news articles
    @Transactional
    public News saveNews(News news) {
        logger.info("Saving news article: " + news.getHeadline());
        deleteOldNews(); // Deletes old news articles before saving new ones
        return newsRepository.save(news);
    }

    // Deletes a news article by its ID
    @Transactional
    public void deleteNews(UUID id) {
        logger.info("Deleting news article with ID: " + id);
        if (newsRepository.existsById(id)) {
            newsRepository.deleteById(id);
        } else {
            throw new RuntimeException("News article with ID " + id + " not found");
        }
    }

    // Updates an existing news article
    @Transactional
    public News updateNews(UUID id, News updatedNews) {
        logger.info("Updating news article with ID: " + id);
        News existingNews = getNewsById(id);
        existingNews.setHeadline(updatedNews.getHeadline());
        existingNews.setDescription(updatedNews.getDescription());
        existingNews.setPublicationTime(updatedNews.getPublicationTime());
        return newsRepository.save(existingNews);
    }

    // Deletes news articles older than one day
    @Transactional
    public void deleteOldNews() {
        LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);
        logger.info("Deleting news articles published before " + oneDayAgo);
        newsRepository.deleteAllByPublicationTimeBefore(oneDayAgo);
        logger.info("Old news articles deletion completed.");
    }
}
