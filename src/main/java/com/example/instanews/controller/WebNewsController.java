package com.example.instanews.controller;

import com.example.instanews.model.News;
import com.example.instanews.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Controller
@RequestMapping("/news")
public class WebNewsController {

    @Autowired
    private NewsService newsService;

    // Display the form for adding a new news item
    @GetMapping("/add")
    public String showAddNewsForm(Model model) {
        model.addAttribute("news", new News()); // Pass a new News object to the model
        return "add-news"; // Return the name of the HTML template for adding news
    }

    // Save the news item submitted through the form
    @PostMapping("/add")
    public String addNews(@ModelAttribute("news") News news) {
        // Set the current date if publication time is not provided
        if (news.getPublicationTime() == null) {
            news.setPublicationTime(LocalDateTime.now());
        }
        newsService.saveNews(news); // Save the news item to the database
        return "redirect:/news"; // Redirect to the news list page after saving
    }

    // Display the list of all news items
    @GetMapping
    public String showNewsList(Model model) {
        model.addAttribute("newsList", newsService.getAllNews()); // Add the list of news items to the model
        return "news"; // Return the template for displaying the list of news
    }

    // Display the form for editing an existing news item
    @GetMapping("/edit/{id}")
    public String showEditNewsForm(@PathVariable UUID id, Model model) {
        News news = newsService.getNewsById(id); // Retrieve the news item by its ID
        model.addAttribute("news", news); // Pass the retrieved news item to the model
        return "add-news"; // Use the same template as for adding news, but with pre-filled data
    }

    // Update the news item submitted through the form
    @PostMapping("/edit/{id}")
    public String updateNews(@PathVariable UUID id, @ModelAttribute("news") News news) {
        // Set the ID of the news item to ensure it is updated
        news.setId(id);
        newsService.updateNews(id, news); // Update the news item in the database
        return "redirect:/news"; // Redirect to the news list page after updating
    }

    // Delete a news item by its ID
    @PostMapping("/delete/{id}")
    public String deleteNews(@PathVariable UUID id) {
        newsService.deleteNews(id); // Delete the news item from the database
        return "redirect:/news"; // Redirect to the news list page after deletion
    }
}
