package com.example.wellnessweb.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @GetMapping("/booksession")
    public ModelAndView getBookSession() {
        ModelAndView mav = new ModelAndView("booktherapysession.html");
        return mav;
    }



}
