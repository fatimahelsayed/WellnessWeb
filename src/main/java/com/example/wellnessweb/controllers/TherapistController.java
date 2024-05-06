package com.example.wellnessweb.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.example.wellnessweb.models.Customer;
import com.example.wellnessweb.models.ReservedTherapySession;
import com.example.wellnessweb.models.Therapist;
import com.example.wellnessweb.models.TherapistRequest;
import com.example.wellnessweb.models.TherapySession;
import com.example.wellnessweb.repositories.BlogsRepository;
import com.example.wellnessweb.repositories.CustomerRepository;
import com.example.wellnessweb.repositories.TherapistRepository;
import com.example.wellnessweb.repositories.TherapySessionRepository;
import com.example.wellnessweb.repositories.ReservedTherapySessionRepository;
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

    @Autowired
    private ReservedTherapySessionRepository reservedTherapySessionRepository;

    private void updateAccountFields(Therapist updatedTherapist, Therapist loggedInTherapist) {
        if (updatedTherapist.getName() != null) {
            loggedInTherapist.setName(updatedTherapist.getName());
        }
        if (updatedTherapist.getAge() != 0) {
            loggedInTherapist.setAge(updatedTherapist.getAge());
        }
        if (updatedTherapist.getEducation() != null && updatedTherapist.getEducation().length() >= 5) {
            loggedInTherapist.setEducation(updatedTherapist.getEducation());
        }
        if (updatedTherapist.getExperience() != null && updatedTherapist.getExperience().length() >= 5) {
            loggedInTherapist.setExperience(updatedTherapist.getExperience());
        }
        if (updatedTherapist.getLanguages() != null && updatedTherapist.getLanguages().length() >= 4) {
            loggedInTherapist.setLanguages(updatedTherapist.getLanguages());
        }
        if (updatedTherapist.getSpecialization() != null && updatedTherapist.getSpecialization().length() >= 4) {
            loggedInTherapist.setSpecialization(updatedTherapist.getSpecialization());
        }
        if (updatedTherapist.getGender() != null) {
            loggedInTherapist.setGender(updatedTherapist.getGender());
        }

        if (updatedTherapist.getEmail() != null) {
            loggedInTherapist.setEmail(updatedTherapist.getEmail());
        }
        if (updatedTherapist.getPassword() != null) {
            loggedInTherapist.setPassword(updatedTherapist.getPassword());
        }
        if (updatedTherapist.getPhoneNumber() != null) {
            loggedInTherapist.setPhoneNumber(updatedTherapist.getPhoneNumber());
        }

    }

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
        List<TherapySession> therapySessions = this.therapySessionRepository
                .findByTherapistID(loggedInTherapist.getID());
        for (TherapySession therapySession : therapySessions) {
            if (this.reservedTherapySessionRepository.existsByTherapySessionID(therapySession.getID())) {
                therapySession.setStatus("RESERVED");
                this.therapySessionRepository.save(therapySession);
            }
        }
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
    public RedirectView addNewSession(@ModelAttribute TherapySession newSession, HttpSession session) {
        Therapist loggedInTherapist = (Therapist) session.getAttribute("loggedInTherapist");
        newSession.setTherapistID(loggedInTherapist.getID());
        newSession.setStatus("UNRESERVED");
        this.therapySessionRepository.save(newSession);
        return new RedirectView("/therapistdashboard/addsession");
    }

    @GetMapping("sessiondetails")
    public ModelAndView editClientForm(@RequestParam("id") int sessionId) {
        ModelAndView mav = new ModelAndView("viewSessionDetailsTherapistDash.html");
        ReservedTherapySession reservedTherapySession = this.reservedTherapySessionRepository
                .findByTherapySessionID(sessionId);
        if (reservedTherapySession != null) {
            TherapySession therapySession = this.therapySessionRepository.findByID(sessionId);
            Customer customer = this.customerRepository.findByID(reservedTherapySession.getCustomerID());
            mav.addObject("therapySess", therapySession);
            mav.addObject("customer", customer);
        } else {
            mav.setViewName("redirect:/therapistdashboard/viewsessions");
        }
        return mav;
    }

    @GetMapping("editaccount")
    public ModelAndView getUpdateAccountForm(HttpSession session) {
        ModelAndView mav = new ModelAndView("updateAccountTherpaistDash.html");
        Therapist loggedInTherapist = (Therapist) session.getAttribute("loggedInTherapist");
        mav.addObject("therapist", loggedInTherapist);
        return mav;
    }

    @PostMapping("editaccount")
    public ModelAndView updateAccount(@ModelAttribute Therapist therapist, BindingResult bindingResult,
            HttpSession session) {
        ModelAndView mav = new ModelAndView("updateAccountTherpaistDash.html");
        Therapist loggedInTherapist = (Therapist) session.getAttribute("loggedInTherapist");

        return mav;
    }

}
