package com.example.wellnessweb.controllers;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.wellnessweb.models.Blogs;
import com.example.wellnessweb.repositories.BlogsRepository;
import com.example.wellnessweb.repositories.IllnessRepository;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
@RequestMapping("/blogs")
public class BlogController {

    @Autowired
    private IllnessRepository illnessRepository;

    @Autowired
    private BlogsRepository blogsRepository;
    

    @GetMapping("addBlog")
    public ModelAndView getBlogForm() {
        ModelAndView mav = new ModelAndView("addBlog.html");
        mav.addObject("illnesses", illnessRepository.findAll());
        Blogs blogObj = new Blogs();
        mav.addObject("blogObj", blogObj); 
        return mav;
    }
    
    @PostMapping("addBlog")
public ModelAndView saveBlog(@ModelAttribute Blogs blogObj, @RequestParam("illnessName") String illnessName) {
     blogObj.setIllnessName(illnessName);
     blogObj.setDate(LocalDate.now());
     blogsRepository.save(blogObj);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/home"); 
        return modelAndView;
    }
}
