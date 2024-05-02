package com.example.wellnessweb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.wellnessweb.models.Illness;
import com.example.wellnessweb.repositories.IllnessRepository;

import java.util.List; 

@RestController
@RequestMapping("/blogs")
public class BlogController {

    @Autowired
    private IllnessRepository illnessRepository;

    @GetMapping("/addBlog")
    public ModelAndView getIllnesses() {
        ModelAndView mav = new ModelAndView("addBlog.html");
        List<Illness> illnesses = this.illnessRepository.findAll();

        mav.addObject("illnesses", illnesses);

        return mav;
    }

}
