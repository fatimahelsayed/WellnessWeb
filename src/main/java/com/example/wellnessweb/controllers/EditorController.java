package com.example.wellnessweb.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class EditorController {

    @GetMapping("/")
    public String showEditor() {
        return "addBlog";
    }
    

    @PostMapping("/save")
    public String saveContent(String content) {
        // Save the content to your database or perform any other action
        System.out.println("Content saved: " + content);
        return "redirect:/";
    }
}
