package com.example.wellnessweb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.wellnessweb.models.Customer;
import com.example.wellnessweb.models.ReservedTherapySession;
import com.example.wellnessweb.models.Therapist;
import com.example.wellnessweb.models.TherapistRequest;
import com.example.wellnessweb.models.TherapySession;
import com.example.wellnessweb.repositories.ReservedTherapySessionRepository;
import com.example.wellnessweb.repositories.TherapistRepository;
import com.example.wellnessweb.repositories.TherapySessionRepository;

import aj.org.objectweb.asm.Attribute;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/profile")
public class UserController {
    @Autowired
    private ReservedTherapySessionRepository reservedTherapySessionRepository;

    @Autowired
    private TherapySessionRepository therapySessionRepository;

    @Autowired
    private TherapistRepository therapistRepository;

    @GetMapping("")
    public ModelAndView getProfile(HttpSession session) {
        if (session.getAttribute("loggedInUser") != null) {
            Customer loggedInUser = (Customer) session.getAttribute("loggedInUser");
            ModelAndView mav = new ModelAndView("userProfile.html");
            mav.addObject("customer", loggedInUser);
            return mav;
        }
        return new ModelAndView("redirect:/login");
    }

    @GetMapping("/bookedSessions")
    public ModelAndView getBookedSessions(HttpSession session) {
        Customer loggedInUser = (Customer) session.getAttribute("loggedInUser");
        ModelAndView mav = new ModelAndView("userProfileBooked.html");
        ReservedTherapySession reservedSession = this.reservedTherapySessionRepository.findByCustomerID(loggedInUser.getID());
        if (reservedSession != null) {
            TherapySession therapySession = this.therapySessionRepository.findById(reservedSession.getTherapySessionID());
            Therapist therapist = this.therapistRepository.findByID(therapySession.getTherapistID());
            mav.addObject("therapist", therapist);
            mav.addObject("therapySession", therapySession);
        }
        mav.addObject("customer", loggedInUser);

        return mav;

    }
}
