package com.example.wellnessweb.controllers;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.wellnessweb.models.Customer;
import com.example.wellnessweb.repositories.CustomerRepository;

@RestController
@RequestMapping("")
public class IndexController {
    @Autowired
    private CustomerRepository customerRepository;


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
        Customer newCustomer = new Customer();
        mav.addObject("customer", newCustomer);
        return mav;
    }

    @PostMapping("/signup")
    public ModelAndView saveClient(@ModelAttribute Customer customer) {
        ModelAndView modelAndView = new ModelAndView();
        String encoddedEmail = BCrypt.hashpw(customer.getEmail(), BCrypt.gensalt(12));
        String encoddedPassword = BCrypt.hashpw(customer.getPassword(), BCrypt.gensalt(12));
        customer.setEmail(encoddedEmail);
        customer.setPassword(encoddedPassword);
        this.customerRepository.save(customer);
        modelAndView.setViewName("redirect:/login");
        return modelAndView;
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
    @GetMapping("/blogs")
    public ModelAndView getBlogs() {
        ModelAndView mav = new ModelAndView("blogs.html");
        return mav;
    }
    @GetMapping("/content")
    public ModelAndView getContent() {
        ModelAndView mav = new ModelAndView("content.html");
        return mav;
    }

    @GetMapping("/editprofile")
    public ModelAndView getEditProfile() {
        ModelAndView mav = new ModelAndView("editUserProfile.html");
        return mav;
    }

    @GetMapping("/profile")
    public ModelAndView getProfile() {
        ModelAndView mav = new ModelAndView("userProfile.html");
        return mav;
    }



}
