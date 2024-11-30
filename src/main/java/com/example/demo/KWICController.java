package com.example.demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class KWICController {

    @Autowired
    private KWICEntryRepository kwicEntryRepository; // Inject the KWIC entry repository
    @Autowired
    private WebPageRepository webPageRepository; // Inject the WebPage repository

    // Generate circular shifts from the input sentence
    public static List<KWICEntry> generateCircularShifts(String sentence) {
        List<KWICEntry> shifts = new ArrayList<>();
        String[] words = sentence.split(" ");
        for (int i = 0; i < words.length; i++) {
            StringBuilder shiftedSentence = new StringBuilder();
            for (int j = i; j < i + words.length; j++) {
                shiftedSentence.append(words[j % words.length]).append(" ");
            }
            shifts.add(new KWICEntry(shiftedSentence.toString().trim()));
        }
        return shifts;
    }

    // Handle GET requests to /kwic
    @GetMapping("/kwic")
    public String kwic(@RequestParam(name = "sentence", required = false, defaultValue = "Web browsers display web pages") String sentence, Model model) {

        // Generate circular shifts
        List<KWICEntry> shifts = generateCircularShifts(sentence);

        // Sort the shifts lexicographically
        List<KWICEntry> sortedShifts = new ArrayList<>(shifts); // Create a copy of the list to sort
        Collections.sort(sortedShifts);

        // Save sorted shifts to MySQL database
        kwicEntryRepository.saveAll(sortedShifts); // Save the sorted shifts to the database

        // Add the shifts and sorted shifts to the model to be displayed in the view
        model.addAttribute("shifts", shifts);
        model.addAttribute("sortedShifts", sortedShifts);
        model.addAttribute("sentence", sentence); // Fixed the model attribute name

        return "kwicResult"; // Make sure this exactly matches the file name without the .html extension
    }

    // Display all sentences stored in the database
    @GetMapping("/allEntries")
    public String getAllEntries(Model model) {
        List<KWICEntry> allEntries = kwicEntryRepository.findAll(); // Fetch all entries from the database
        model.addAttribute("allEntries", allEntries);
        return "allEntries"; // Corresponding Thymeleaf template for allEntries
    }

    // Search for a keyword in the stored WebPages' descriptions
    @GetMapping("/search")
    public String searchEntries(@RequestParam(name = "keyword") String keyword, Model model) {
        // Search for WebPages containing the keyword in their descriptions
        List<WebPage> searchResults = webPageRepository.findByDescriptionContainingIgnoreCase(keyword);
        model.addAttribute("searchResults", searchResults);
        model.addAttribute("searchTerm", keyword);
        return "searchResults"; // Corresponding Thymeleaf template for search results
    }
}
