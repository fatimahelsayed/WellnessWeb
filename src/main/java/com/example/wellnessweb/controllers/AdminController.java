package com.example.wellnessweb.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.wellnessweb.models.Customer;
import com.example.wellnessweb.models.TherapistRequest;
import com.example.wellnessweb.repositories.BlogsRepository;
import com.example.wellnessweb.repositories.CustomerRepository;
import com.example.wellnessweb.repositories.TherapistRepository;
import com.example.wellnessweb.repositories.TherapistRequestRepository;

@RestController
@RequestMapping("")
public class AdminController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BlogsRepository blogsRepository;

    @Autowired
    private TherapistRepository therapistRepository;

    @Autowired TherapistRequestRepository therapistRequestRepository;

    @GetMapping("/admindashboard")
    public ModelAndView getAdminDashboard() {
        ModelAndView mav = new ModelAndView("admindashboard.html");
        
        List<Customer> recentCustomers = this.customerRepository.findTop5ByOrderByCreatedAtDesc();
        mav.addObject("recentCustomers", recentCustomers);

        List<TherapistRequest> recentRequests = this.therapistRequestRepository.findTop5ByOrderByCreatedAtDesc();
        mav.addObject("recentRequests", recentRequests);

        long blogCount = this.blogsRepository.count();
        long customerCount = this.customerRepository.count();
        long therapistCount = this.therapistRepository.count();
        
        mav.addObject("blogCount", blogCount);
        mav.addObject("customerCount", customerCount);
        mav.addObject("therapistCount", therapistCount);
        
        return mav;
    }
    
}
