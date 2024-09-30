package com.example.instanews;

import com.example.instanews.model.News;
import com.example.instanews.service.NewsService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@SpringBootTest
class NewsServiceTests {

    // Mock the NewsService to simulate its behavior in tests
    @MockBean
    private NewsService newsService;

    // Test for retrieving all news items
    @Test
    void getAllNewsTest() {
        // Create a sample list of news items for testing
        List<News> expectedNewsList = Arrays.asList(
                new News("Headline 1", "Description 1", LocalDateTime.now()),
                new News("Headline 2", "Description 2", LocalDateTime.now())
        );

        // When the service method is called, return the sample list
        when(newsService.getAllNews()).thenReturn(expectedNewsList);

        // Call the method to get actual news items
        List<News> actualNewsList = newsService.getAllNews();

        // Verify that the size of the actual news list matches the expected size
        assertEquals(expectedNewsList.size(), actualNewsList.size());
        // Verify that the service method was called once
        verify(newsService, times(1)).getAllNews();
    }

    // Test for adding a news item
    @Test
    void addNewsTest() {
        // Create a sample news item to be added
        News news = new News("Headline", "Description", LocalDateTime.now());

        // When the service method is called, return the sample news item
        when(newsService.saveNews(news)).thenReturn(news);

        // Call the method to save the news item
        News savedNews = newsService.saveNews(news);

        // Verify that the saved news item is not null and has the correct headline
        assertNotNull(savedNews);
        assertEquals("Headline", savedNews.getHeadline());
        // Verify that the service method was called once
        verify(newsService, times(1)).saveNews(news);
    }

    // Test for updating a news item
    @Test
    void updateNewsTest() {
        // Create a UUID for the news item to be updated
        UUID id = UUID.randomUUID();
        // Create a sample updated news item
        News updatedNews = new News("Updated Headline", "Updated Description", LocalDateTime.now());

        // When the service method is called, return the updated news item
        when(newsService.updateNews(eq(id), any(News.class))).thenReturn(updatedNews);

        // Call the method to update the news item
        News result = newsService.updateNews(id, updatedNews);

        // Verify that the updated news item is not null and has the correct headline
        assertNotNull(result);
        assertEquals("Updated Headline", result.getHeadline());
        // Verify that the service method was called once
        verify(newsService, times(1)).updateNews(eq(id), any(News.class));
    }

    // Test for deleting a news item
    @Test
    void deleteNewsTest() {
        // Create a UUID for the news item to be deleted
        UUID id = UUID.randomUUID();

        // Simulate the delete action without doing anything
        doNothing().when(newsService).deleteNews(id);

        // Call the method to delete the news item
        newsService.deleteNews(id);

        // Verify that the service method was called once
        verify(newsService, times(1)).deleteNews(id);
    }
}
