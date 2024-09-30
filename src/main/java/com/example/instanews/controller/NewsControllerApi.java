package com.example.instanews.controller;

import com.example.instanews.model.News;
import com.example.instanews.service.NewsService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class NewsControllerApi {

    @FXML
    private Label headlineLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Button nextButton;

    @FXML
    private Button prevButton;

    @FXML
    private Button addButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TextField headlineField;

    @FXML
    private TextField descriptionField;

    @Autowired
    private NewsService newsService;

    private List<News> newsList; // List to store news items
    private int currentIndex = 0; // Current index for navigating through the news list

    // Initialize method is called when the UI components are loaded
    @FXML
    public void initialize() {
        loadNews(); // Load all news items from the service
        updateNewsDisplay(); // Display the first news item

        // Set actions for buttons
        nextButton.setOnAction(e -> showNextNews());
        prevButton.setOnAction(e -> showPreviousNews());
        addButton.setOnAction(e -> addNews());
        editButton.setOnAction(e -> editNews());
        deleteButton.setOnAction(e -> deleteNews());

        updateButtonState(); // Update the state of buttons
    }

    // Load all news items from the service into the news list
    private void loadNews() {
        newsList = newsService.getAllNews();
    }

    // Show the next news item in the list
    private void showNextNews() {
        if (currentIndex < newsList.size() - 1) {
            currentIndex++;
            updateNewsDisplay();
            updateButtonState();
        }
    }

    // Show the previous news item in the list
    private void showPreviousNews() {
        if (currentIndex > 0) {
            currentIndex--;
            updateNewsDisplay();
            updateButtonState();
        }
    }

    // Update the displayed news item based on the current index
    private void updateNewsDisplay() {
        if (!newsList.isEmpty()) {
            News news = newsList.get(currentIndex);
            headlineLabel.setText(news.getHeadline());
            descriptionLabel.setText(news.getDescription());
        } else {
            headlineLabel.setText("No news available.");
            descriptionLabel.setText("");
        }
    }

    // Update the state of navigation buttons based on the current index
    private void updateButtonState() {
        prevButton.setDisable(currentIndex == 0);
        nextButton.setDisable(currentIndex == newsList.size() - 1);
    }

    // Add a new news item using the text fields for headline and description
    private void addNews() {
        String headline = headlineField.getText();
        String description = descriptionField.getText();

        if (!headline.isEmpty() && !description.isEmpty()) {
            News newNews = new News();
            newNews.setHeadline(headline);
            newNews.setDescription(description);
            newsService.saveNews(newNews); // Save the new news item
            loadNews(); // Reload the news list
            updateNewsDisplay(); // Update the displayed news item
        }
    }

    // Edit the current news item
    @FXML
    private void editNews() {
        if (!newsList.isEmpty() && currentIndex >= 0 && currentIndex < newsList.size()) {
            News currentNews = newsList.get(currentIndex);
            currentNews.setHeadline(headlineField.getText());
            currentNews.setDescription(descriptionField.getText());

            // Pass the UUID of the current news item for updating
            newsService.updateNews(currentNews.getId(), currentNews);

            updateNewsDisplay(); // Update the displayed news item
        }
    }

    // Delete the current news item
    private void deleteNews() {
        if (!newsList.isEmpty()) {
            News currentNews = newsList.get(currentIndex);
            newsService.deleteNews(currentNews.getId()); // Delete the news item from the service

            // Remove the current news item from the list
            newsList.remove(currentIndex);

            // Adjust the index if the current index exceeds the list size
            if (currentIndex >= newsList.size() && currentIndex > 0) {
                currentIndex--;
            }

            updateNewsDisplay(); // Update the displayed news item
            updateButtonState(); // Update the state of navigation buttons
        }
    }
}
