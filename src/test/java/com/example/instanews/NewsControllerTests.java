package com.example.instanews;

import com.example.instanews.controller.NewsController;
import com.example.instanews.model.News;
import com.example.instanews.service.NewsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
class NewsControllerTests {

    // MockMvc object for testing the NewsController
    private MockMvc mockMvc;

    // Mock of the NewsService to simulate its behavior in tests
    @Mock
    private NewsService newsService;

    // Inject the mocked NewsService into the NewsController for testing
    @InjectMocks
    private NewsController newsController;

    // Set up the test environment before each test
    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
        // Build MockMvc with the controller under test
        mockMvc = MockMvcBuilders.standaloneSetup(newsController).build();
    }

    // Test for getting all news items
    @Test
    void getAllNewsTest() throws Exception {
        // Create a sample list of news items
        List<News> newsList = Arrays.asList(
                new News("Headline 1", "Description 1", LocalDateTime.now()),
                new News("Headline 2", "Description 2", LocalDateTime.now())
        );

        // When the service method is called, return the sample list
        when(newsService.getAllNews()).thenReturn(newsList);

        // Perform a GET request and verify the response
        mockMvc.perform(get("/api/news")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Expect HTTP status 200 OK
                .andExpect(jsonPath("$", hasSize(2))); // Expect the response to contain 2 items

        // Verify that the service method was called once
        verify(newsService, times(1)).getAllNews();
    }

    // Test for adding a news item
    @Test
    void addNewsTest() throws Exception {
        // Create a sample news item to be added
        News news = new News("Headline", "Description", LocalDateTime.now());
        news.setId(UUID.randomUUID()); // Assign a random UUID

        // When the service method is called, return the sample news item
        when(newsService.saveNews(any(News.class))).thenReturn(news);

        // Perform a POST request to add the news item and verify the response
        mockMvc.perform(post("/api/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"headline\":\"Headline\", \"description\":\"Description\"}"))
                .andExpect(status().isOk()) // Expect HTTP status 200 OK
                .andExpect(jsonPath("$.headline").value("Headline")); // Expect the headline in the response

        // Verify that the service method was called once
        verify(newsService, times(1)).saveNews(any(News.class));
    }

    // Test for updating a news item
    @Test
    void updateNewsTest() throws Exception {
        // Create a UUID for the news item to be updated
        UUID id = UUID.randomUUID();
        // Create a sample updated news item
        News updatedNews = new News("Updated Headline", "Updated Description", LocalDateTime.now());

        // When the service method is called, return the updated news item
        when(newsService.updateNews(eq(id), any(News.class))).thenReturn(updatedNews);

        // Perform a PUT request to update the news item and verify the response
        mockMvc.perform(put("/api/news/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"headline\":\"Updated Headline\", \"description\":\"Updated Description\"}"))
                .andExpect(status().isOk()) // Expect HTTP status 200 OK
                .andExpect(jsonPath("$.headline").value("Updated Headline")); // Expect the updated headline in the response

        // Verify that the service method was called once
        verify(newsService, times(1)).updateNews(eq(id), any(News.class));
    }

    // Test for deleting a news item
    @Test
    void deleteNewsTest() throws Exception {
        // Create a UUID for the news item to be deleted
        UUID id = UUID.randomUUID();

        // Simulate the delete action without doing anything
        doNothing().when(newsService).deleteNews(id);

        // Perform a DELETE request to remove the news item and verify the response
        mockMvc.perform(delete("/api/news/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); // Expect HTTP status 200 OK

        // Verify that the service method was called once
        verify(newsService, times(1)).deleteNews(id);
    }
}
