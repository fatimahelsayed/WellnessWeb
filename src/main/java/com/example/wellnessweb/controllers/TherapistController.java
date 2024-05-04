package com.example.wellnessweb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.wellnessweb.models.Therapist;
import com.example.wellnessweb.models.TherapistRequest;
import com.example.wellnessweb.repositories.BlogsRepository;
import com.example.wellnessweb.repositories.CustomerRepository;
import com.example.wellnessweb.repositories.TherapistRepository;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/therapistdashboard")
public class TherapistController {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BlogsRepository blogsRepository;

    @Autowired
    private TherapistRepository therapistRepository;

    @GetMapping("")
    public ModelAndView getTherapistDashboard(HttpSession session) {

        ModelAndView mav = new ModelAndView("therapistDash.html");
        Therapist loggedInTherapist = (Therapist) session.getAttribute("loggedInTherapist");
        TherapistRequest therapistRequest = (TherapistRequest) session.getAttribute("therapistReq");
        mav.addObject("therapistReq", therapistRequest);
        mav.addObject("therapist", loggedInTherapist);
        return mav;
    }
    

}
