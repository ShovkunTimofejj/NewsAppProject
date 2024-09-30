package com.example.instanews.scheduler;

import com.example.instanews.model.News;
import com.example.instanews.service.NewsService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class NewsScheduler {

    private static final Logger logger = LoggerFactory.getLogger(NewsScheduler.class);

    @Autowired
    private NewsService newsService;

    // Scheduled method that fetches news from Hacker News every 20 minutes
    @Scheduled(fixedRate = 1200000) // every 20 minutes
    @Transactional
    public void fetchNews() {
        try {
            logger.info("Starting to parse the Hacker News page.");
            Document doc = Jsoup.connect("https://news.ycombinator.com/").get();
            logger.info("Page successfully loaded.");

            // Extract all news elements
            Elements storyLinks = doc.select("span.titleline > a");
            Elements timeElements = doc.select("span.age");

            if (storyLinks.isEmpty() || timeElements.isEmpty()) {
                logger.warn("Failed to find any news on the page.");
                return;
            }

            for (int i = 0; i < storyLinks.size(); i++) {
                // Extract headline and link
                Element titleElement = storyLinks.get(i);
                String headline = titleElement != null ? titleElement.text() : "";
                String link = titleElement.attr("href");

                // Description of the news - left empty since there is no description on the site
                String description = ""; // You can remove this field if not needed

                // Extract publication time from the attribute "title"
                Element timeElement = timeElements.get(i);
                String timeStr = timeElement != null ? timeElement.attr("title") : null;

                if (timeStr == null) {
                    logger.warn("Skipped news without a publication time: " + headline);
                    continue;
                }

                // Convert the time string to LocalDateTime
                LocalDateTime publicationTime = parsePublicationTime(timeStr);
                if (publicationTime == null) {
                    logger.warn("Error parsing publication time for news: " + headline);
                    continue;
                }

                // Save the news
                News news = new News(headline, description, publicationTime);
                newsService.saveNews(news);
                logger.info("Saved news: " + headline + " | Link: " + link + " | Publication time: " + publicationTime);
            }
        } catch (IOException e) {
            logger.error("Error while parsing news: ", e);
        }
    }

    // Helper method to parse the publication time from a string to LocalDateTime
    private LocalDateTime parsePublicationTime(String timeStr) {
        try {
            // Parse time from an ISO-8601 formatted string
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            return LocalDateTime.parse(timeStr, formatter);
        } catch (Exception e) {
            logger.error("Error while parsing publication time: ", e);
            return null;
        }
    }
}
