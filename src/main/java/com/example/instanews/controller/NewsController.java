package com.example.instanews.controller;

import com.example.instanews.model.News;
import com.example.instanews.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/news")
@CrossOrigin(origins = "*") // Allow requests from any origin, can specify a specific domain for better security
public class NewsController {

    @Autowired
    private NewsService newsService;

    // Retrieve all news
    @GetMapping
    public List<News> getAllNews() {
        return newsService.getAllNews();
    }

    // Retrieve news within a specified time period
    @GetMapping("/filter")
    public List<News> getNewsByTimePeriod(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return newsService.getNewsByTimePeriod(start, end);
    }

    // Filter news by time of day (morning, afternoon, evening)
    @GetMapping("/filter/timeOfDay")
    public List<News> getNewsByTimeOfDay(@RequestParam("timePeriod") String timePeriod) {
        return newsService.getNewsByTimeOfDay(timePeriod);
    }

    // Add a new news item
    @PostMapping
    public News addNews(@RequestBody News news) {
        // Set the current date if publication time is not provided
        if (news.getPublicationTime() == null) {
            news.setPublicationTime(LocalDateTime.now());
        }
        return newsService.saveNews(news);
    }

    // Update an existing news item
    @PutMapping("/{id}")
    public News updateNews(@PathVariable UUID id, @RequestBody News news) {
        return newsService.updateNews(id, news);
    }

    // Retrieve a news item by ID
    @GetMapping("/{id}")
    public News getNewsById(@PathVariable UUID id) {
        return newsService.getNewsById(id);
    }

    // Delete a news item by ID
    @DeleteMapping("/{id}")
    public void deleteNews(@PathVariable UUID id) {
        newsService.deleteNews(id);
    }
}
