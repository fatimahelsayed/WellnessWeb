package com.example.wellnessweb.controllers;

import java.io.File;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
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

    private String handleImageUpload(MultipartFile file, String phoneNumber) {
        String imageName = null;
        try {
            if (!file.isEmpty()) {
                String originalFilename = file.getOriginalFilename();
                String fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.')).toLowerCase();

                // Check if the file extension is one of the allowed image formats
                if (fileExtension.equals(".jpg") || fileExtension.equals(".jpeg") || fileExtension.equals(".png")
                        || fileExtension.equals(".gif")) {
                    String newFileName = phoneNumber + fileExtension;
                    String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/images/";
                    String imagePath = uploadDir + newFileName;

                    File destImage = new File(imagePath);
                    file.transferTo(destImage);
                    imageName = newFileName;
                } else {
                    System.out.println("Invalid image format: " + fileExtension);
                }
            }
        } catch (Exception e) {
            System.out.println("Error storing image: " + e.getMessage());
        }
        return imageName;
    }

    private Boolean updateAccountFields(Therapist updatedTherapist, Therapist loggedInTherapist) {
        Boolean isPasswordMatched = BCrypt.checkpw(updatedTherapist.getPassword(), loggedInTherapist.getPassword());
        Boolean valid = true;
        if (isPasswordMatched) {
            if (updatedTherapist.getName() != null) {
                loggedInTherapist.setName(updatedTherapist.getName());
            }
            if (updatedTherapist.getAge() != 0) {
                loggedInTherapist.setAge(updatedTherapist.getAge());
            }
            if (updatedTherapist.getEducation() != null && updatedTherapist.getEducation().length() >= 3) {
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
                String encoddedPassword = BCrypt.hashpw(updatedTherapist.getPassword(), BCrypt.gensalt(12));
                loggedInTherapist.setPassword(encoddedPassword);
            }
            if (updatedTherapist.getPhoneNumber() != null) {
                loggedInTherapist.setPhoneNumber(updatedTherapist.getPhoneNumber());
            }

        } else {
            valid = false;
        }
        return valid;
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
        ModelAndView mav = new ModelAndView("updateAccountTherapistDash.html");
        Therapist loggedInTherapist = (Therapist) session.getAttribute("loggedInTherapist");
        loggedInTherapist.setPassword("");
        mav.addObject("therapist", loggedInTherapist);
        return mav;
    }

    @PostMapping("editaccount")
    public ModelAndView updateAccount(@ModelAttribute Therapist therapist, BindingResult bindingResult,
            @RequestParam("imageFile") MultipartFile imageFile, HttpSession session) {
        ModelAndView mav = new ModelAndView("updateAccountTherpaistDash.html");
        Therapist loggedInTherapist = (Therapist) session.getAttribute("loggedInTherapist");
        Boolean valid = updateAccountFields(therapist, loggedInTherapist);
        String imageName = handleImageUpload(imageFile, loggedInTherapist.getPhoneNumber());
        if (imageName != null) {
            loggedInTherapist.setImage(imageName);
        }
        if (valid) {
            this.therapistRepository.save(loggedInTherapist);
            mav.setViewName("redirect:/therapistdashboard");
        }
        return mav;
    }

    @GetMapping("changepassword")
    public ModelAndView getChangePasswordForm(HttpSession session) {
        ModelAndView mav = new ModelAndView("changePasswordTherapistDash.html");
        Therapist loggedInTherapist = (Therapist) session.getAttribute("loggedInTherapist");
        mav.addObject("therapist", loggedInTherapist);
        return mav;
    }

    @PostMapping("changepassword")
    public ModelAndView changePassword(@RequestParam("Confirmed-Password") String password, HttpSession session) {
        ModelAndView mav = new ModelAndView("changePasswordTherapistDash.html");
        Therapist loggedInTherapist = (Therapist) session.getAttribute("loggedInTherapist");
        String encoddedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));
        loggedInTherapist.setPassword(encoddedPassword);
        this.therapistRepository.save(loggedInTherapist);
        mav.setViewName("redirect:/therapistdashboard/editaccount");
        return mav;
    }

}
