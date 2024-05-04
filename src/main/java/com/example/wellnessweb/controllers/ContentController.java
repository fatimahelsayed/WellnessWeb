package com.example.wellnessweb.controllers;

import java.io.IOException;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.wellnessweb.models.Blogs;
import com.example.wellnessweb.models.Content;

import com.example.wellnessweb.repositories.BlogsRepository;
import com.example.wellnessweb.repositories.IllnessRepository;
import com.example.wellnessweb.repositories.ContentRepository;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
@RequestMapping("/content")

public class ContentController {

    @Autowired
    private IllnessRepository illnessRepository;

    @Autowired
    private ContentRepository contentRepository;

    @GetMapping("addContent")
    public ModelAndView getArticleForm() {
        ModelAndView mav = new ModelAndView("addContent.html");
        mav.addObject("illnesses", illnessRepository.findAll());
        Content articleObj = new Content();
        mav.addObject("articleObj", articleObj);
        return mav;
    }

    @PostMapping("addContent")
    public ModelAndView saveArticle(@ModelAttribute Content articleObj, 
                                    @RequestParam("illnessName") String illnessName,
                                    @RequestParam("file") MultipartFile file) {

        try {
            articleObj.setIllnessName(illnessName);
            articleObj.setDate(LocalDate.now());
            
            if (!file.isEmpty()) {
                articleObj.setImage(file.getBytes());
            }

            contentRepository.save(articleObj);
            return new ModelAndView("redirect:/home");
        } catch (IOException e) {
            e.printStackTrace();
            // Handle file upload error
            return new ModelAndView("error.html");
        }
    }

}
