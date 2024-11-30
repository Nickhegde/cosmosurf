package com.example.demo;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
 


public class SearchController {
    @Autowired
    private WebPageRepository webPageRepository;
 
    @GetMapping("/searchkeyword")
    public List<WebPage> search(@RequestParam String keyword) {
        System.out.println("hello in searchcontroller");
        return webPageRepository.findByDescriptionContainingIgnoreCase(keyword);
    }
}
