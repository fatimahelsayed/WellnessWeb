package com.example.wellnessweb.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("")
public class IndexController {
    @GetMapping("/home")
    public ModelAndView getHome() {
        ModelAndView mav = new ModelAndView("home.html");
        return mav;
    }
    @GetMapping("/login")
    public ModelAndView getLogin() {
        ModelAndView mav = new ModelAndView("login.html");
        return mav;
    }
    @GetMapping("/signup")
    public ModelAndView getSignUp() {
        ModelAndView mav = new ModelAndView("signup.html");
        return mav;
    }
    @GetMapping("/therapistapply")
    public ModelAndView getTherapistApply() {
        ModelAndView mav = new ModelAndView("therapistApply.html");
        return mav;
    }
    @GetMapping("/therapists")
    public ModelAndView getTherapists() {
        ModelAndView mav = new ModelAndView("viewtherapists.html");
        return mav;
    }
    @GetMapping("/booksession")
    public ModelAndView getBookSession() {
        ModelAndView mav = new ModelAndView("booktherapysession.html");
        return mav;
    }
}
