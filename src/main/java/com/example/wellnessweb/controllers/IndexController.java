package com.example.wellnessweb.controllers;

import java.io.File;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.example.wellnessweb.models.Customer;
import com.example.wellnessweb.models.TherapistRequest;
import com.example.wellnessweb.repositories.CustomerRepository;
import com.example.wellnessweb.repositories.TherapistRequestRepository;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("")
public class IndexController {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TherapistRequestRepository therapistRequestRepository;

    @Autowired
    private ServletContext servletContext;

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

    @PostMapping("/login")
    public RedirectView loginProcess(@RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpSession session) {
        Customer dbCustomer = this.customerRepository.findByUsername(username);
        if (dbCustomer != null) {
            Boolean isPasswordMatched = BCrypt.checkpw(password, dbCustomer.getPassword());
            if (isPasswordMatched) {
                session.setAttribute("loggedInUser", dbCustomer);
                return new RedirectView("/profile");
            } else {
                return new RedirectView("/login?error=wrongPassword");
            }
        } else {
            return new RedirectView("/login?error=userNotFound");

        }
    }

    @GetMapping("/signup")
    public ModelAndView getSignUp() {
        ModelAndView mav = new ModelAndView("signup.html");
        Customer newCustomer = new Customer();
        mav.addObject("customer", newCustomer);
        return mav;
    }
    @PostMapping("/signup")
    public ModelAndView saveCustomer(@ModelAttribute Customer customer) {
        ModelAndView modelAndView = new ModelAndView();

        boolean emailExists = customerRepository.existsByEmail(customer.getEmail());
        boolean phoneExists = customerRepository.existsByPhoneNumber(customer.getPhoneNumber());
        boolean usernameExists = customerRepository.existsByUsername(customer.getUsername());
    
        if (emailExists) {
            modelAndView.setViewName("redirect:/signup?EmailExists");
            return modelAndView;
        } else if( phoneExists){
            modelAndView.setViewName("redirect:/signup?PhoneNumberExists");
            return modelAndView;
        }else if (usernameExists) {
            modelAndView.setViewName("redirect:/signup?UsernameTaken");
            return modelAndView;
        }else {
            String encoddedPassword = BCrypt.hashpw(customer.getPassword(), BCrypt.gensalt(12));
            customer.setPassword(encoddedPassword);
            this.customerRepository.save(customer);
            modelAndView.setViewName("redirect:/login");
        }
    
        return modelAndView;
    }


    @GetMapping("/therapistapply")
    public ModelAndView getTherapistApply() {
        ModelAndView mav = new ModelAndView("therapistApply.html");
        TherapistRequest newRequest = new TherapistRequest();
        mav.addObject("request", newRequest);
        return mav;
    }

    @PostMapping("/therapistapply")
    public ModelAndView saveTherapistRequest(@ModelAttribute TherapistRequest request,
            @RequestParam("file") MultipartFile file) {
    
        ModelAndView modelAndView = new ModelAndView();
        String encoddedPassword = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt(12));
        request.setPassword(encoddedPassword);
        request.setIsAccepted("Pending");
    
        try {
            boolean emailExists = therapistRequestRepository.existsByEmail(request.getEmail());
            boolean phoneNumberExists = therapistRequestRepository.existsByPhoneNumber(request.getPhoneNumber());
    
            if (emailExists || phoneNumberExists) {
                modelAndView.setViewName("redirect:/therapistapply?RequestAlreadySent");
                return modelAndView;
            }
    
            String fileName = handleFileUpload(file, request);
    
            if (fileName != null) {
                request.setResume(fileName);
            }
    
            this.therapistRequestRepository.save(request);
            modelAndView.setViewName("redirect:/therapistapply?RequestSent");
        } catch (Exception e) {
            System.out.println("Error adding class: " + e.getMessage());
            modelAndView.setViewName("error_page");
        }
    
        return modelAndView;
    }
    

    private String handleFileUpload(MultipartFile file, TherapistRequest request) {
        String filePath = null;
        String fileName = null;
        try {
            if (!file.isEmpty()) {
                fileName = request.getPhoneNumber() + ".pdf"; 
                String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/cv/";
                filePath = uploadDir + fileName;

                // Save the uploaded file to the desired location
                File destFile = new File(filePath);
                file.transferTo(destFile);
            }
        } catch (Exception e) {
            System.out.println("Error storing file: " + e.getMessage());
        }
        return fileName;
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
