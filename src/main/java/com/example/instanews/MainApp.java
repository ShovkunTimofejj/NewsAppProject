package com.example.instanews;

import com.example.instanews.model.News;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

public class MainApp extends Application {

    private int currentIndex = 0; // Index to track the currently displayed news article
    private ObservableList<News> newsList = FXCollections.observableArrayList(); // List to hold news articles

    private ComboBox<String> timePeriodComboBox = new ComboBox<>(); // ComboBox for selecting time period

    private Label headlineLabel = new Label(); // Label to display the news headline
    private Label descriptionLabel = new Label(); // Label to display the news description
    private TextField headlineField = new TextField(); // TextField for inputting the headline
    private TextField descriptionField = new TextField(); // TextField for inputting the description

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(); // VBox layout to hold UI elements
        Button nextButton = new Button("Next News"); // Button to show the next news article
        Button prevButton = new Button("Previous News"); // Button to show the previous news article
        Button addButton = new Button("Add News"); // Button to add a new news article
        Button editButton = new Button("Edit News"); // Button to edit the current news article
        Button deleteButton = new Button("Delete News"); // Button to delete the current news article

        // Populate the ComboBox with time periods
        timePeriodComboBox.getItems().addAll("morning", "day", "evening");
        timePeriodComboBox.setOnAction(e -> loadNewsByTimePeriod()); // Load news when a time period is selected

        loadNewsAsync(); // Asynchronously load news articles at startup

        // Set action events for buttons
        prevButton.setOnAction(e -> showPreviousNews());
        nextButton.setOnAction(e -> showNextNews());
        addButton.setOnAction(e -> addNews());
        editButton.setOnAction(e -> editNews());
        deleteButton.setOnAction(e -> deleteNews());

        // Add UI elements to the layout
        root.getChildren().addAll(
                headlineLabel, descriptionLabel,
                new Label("Headline:"), headlineField,
                new Label("Description:"), descriptionField,
                timePeriodComboBox, prevButton, nextButton, addButton, editButton, deleteButton
        );

        // Set up the scene and show the primary stage
        Scene scene = new Scene(root, 400, 500);
        primaryStage.setTitle("News App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Asynchronously load news articles
    private void loadNewsAsync() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                loadNews(); // Call the method to load news
                return null;
            }
        };
        new Thread(task).start(); // Start a new thread to execute the task
    }

    // Load news articles from the API
    private void loadNews() {
        RestTemplate restTemplate = new RestTemplate();
        News[] newsArray = restTemplate.getForObject("http://localhost:8080/api/news", News[].class); // Fetch news articles
        if (newsArray != null) {
            javafx.application.Platform.runLater(() -> {
                newsList.clear(); // Clear the existing list
                newsList.addAll(Arrays.asList(newsArray)); // Add new articles to the list
                if (!newsList.isEmpty()) {
                    displayNews(0); // Display the first news article if the list is not empty
                }
            });
        }
    }

    // Load news articles based on the selected time period
    private void loadNewsByTimePeriod() {
        String timePeriod = timePeriodComboBox.getValue();
        if (timePeriod != null) {
            RestTemplate restTemplate = new RestTemplate();
            News[] newsArray = restTemplate.getForObject("http://localhost:8080/api/news/filter/timeOfDay?timePeriod=" + timePeriod, News[].class);
            if (newsArray != null) {
                javafx.application.Platform.runLater(() -> {
                    newsList.clear(); // Clear the existing list
                    newsList.addAll(Arrays.asList(newsArray)); // Add new articles to the list
                    if (!newsList.isEmpty()) {
                        displayNews(0); // Display the first news article if the list is not empty
                    }
                });
            }
        }
    }

    // Show the previous news article
    private void showPreviousNews() {
        if (currentIndex > 0) {
            currentIndex--; // Decrement the index
            displayNews(currentIndex); // Display the news article at the current index
        }
    }

    // Show the next news article
    private void showNextNews() {
        if (currentIndex < newsList.size() - 1) {
            currentIndex++; // Increment the index
            displayNews(currentIndex); // Display the news article at the current index
        }
    }

    // Add a new news article
    private void addNews() {
        String headline = headlineField.getText();
        String description = descriptionField.getText();

        // Check if the headline and description are not empty
        if (!headline.isEmpty() && !description.isEmpty()) {
            RestTemplate restTemplate = new RestTemplate();
            News newNews = new News(); // Create a new News object
            newNews.setHeadline(headline);
            newNews.setDescription(description);
            restTemplate.postForObject("http://localhost:8080/api/news", newNews, News.class); // Send a POST request to add the news
            loadNews(); // Reload news articles after adding
        }
    }

    // Edit the current news article
    private void editNews() {
        if (!newsList.isEmpty()) {
            String headline = headlineField.getText();
            String description = descriptionField.getText();

            // Check if the headline and description are not empty
            if (!headline.isEmpty() && !description.isEmpty()) {
                RestTemplate restTemplate = new RestTemplate();
                News currentNews = newsList.get(currentIndex); // Get the current news article
                currentNews.setHeadline(headline);
                currentNews.setDescription(description);
                restTemplate.put("http://localhost:8080/api/news/" + currentNews.getId(), currentNews); // Send a PUT request to update the news
                loadNews(); // Reload news articles after editing
            }
        }
    }

    // Delete the current news article
    private void deleteNews() {
        if (!newsList.isEmpty()) {
            RestTemplate restTemplate = new RestTemplate();
            News currentNews = newsList.get(currentIndex); // Get the current news article
            restTemplate.delete("http://localhost:8080/api/news/" + currentNews.getId()); // Send a DELETE request to remove the news
            loadNews(); // Reload news articles after deletion
            // Adjust currentIndex if it exceeds the size of the list
            if (currentIndex >= newsList.size() && currentIndex > 0) {
                currentIndex--;
            }
            displayNews(currentIndex); // Display the news article at the updated index
        }
    }

    // Display a news article at the specified index
    private void displayNews(int index) {
        if (index >= 0 && index < newsList.size()) {
            News news = newsList.get(index); // Get the news article at the index
            javafx.application.Platform.runLater(() -> {
                headlineLabel.setText(news.getHeadline()); // Update the headline label
                descriptionLabel.setText(news.getDescription()); // Update the description label
            });
        }
    }

    // Launch the JavaFX application
    public static void launchApp(Class<? extends Application> appClass, String[] args) {
        Application.launch(appClass, args);
    }

    // Main method to start the application
    public static void main(String[] args) {
        launch(args);
    }
}
