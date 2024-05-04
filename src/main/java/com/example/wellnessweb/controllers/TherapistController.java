package com.example.wellnessweb.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.example.wellnessweb.models.Therapist;
import com.example.wellnessweb.models.TherapistRequest;
import com.example.wellnessweb.models.TherapySession;
import com.example.wellnessweb.repositories.BlogsRepository;
import com.example.wellnessweb.repositories.CustomerRepository;
import com.example.wellnessweb.repositories.TherapistRepository;
import com.example.wellnessweb.repositories.TherapySessionRepository;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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

    @Autowired
    private TherapySessionRepository therapySessionRepository;

    @GetMapping("")
    public ModelAndView getTherapistDashboard(HttpSession session) {

        ModelAndView mav = new ModelAndView("therapistDash.html");
        Therapist loggedInTherapist = (Therapist) session.getAttribute("loggedInTherapist");
        TherapistRequest therapistRequest = (TherapistRequest) session.getAttribute("therapistReq");
        mav.addObject("therapistReq", therapistRequest);
        mav.addObject("therapist", loggedInTherapist);
        return mav;
    }
    @GetMapping("viewsessions")
    public ModelAndView getTherapySessions(HttpSession session) {
        ModelAndView mav = new ModelAndView("sessionsTherapistDash.html");
        Therapist loggedInTherapist = (Therapist) session.getAttribute("loggedInTherapist");
        List<TherapySession> therapySessions = this.therapySessionRepository.findByTherapistID(loggedInTherapist.getID());
        mav.addObject("therapySessions", therapySessions);
        return mav;
    }
    @GetMapping("addsession")
    public ModelAndView getSessionForm(HttpSession session) {
        ModelAndView mav = new ModelAndView("addSessionTherapistDash.html");
        Therapist loggedInTherapist = (Therapist) session.getAttribute("loggedInTherapist");
        mav.addObject("newSession", new TherapySession());
        return mav;
    }
    @PostMapping("addsession")
    public RedirectView addNewSession(@ModelAttribute TherapySession newSession ,HttpSession session) {
        Therapist loggedInTherapist = (Therapist) session.getAttribute("loggedInTherapist");
        newSession.setTherapistID(loggedInTherapist.getID());
        this.therapySessionRepository.save(newSession);
        return new RedirectView("/therapistdashboard/addsession");
    }
    

}
