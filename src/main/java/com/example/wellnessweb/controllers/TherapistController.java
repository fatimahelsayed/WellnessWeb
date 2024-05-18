package com.example.wellnessweb.controllers;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.example.wellnessweb.models.Customer;
import com.example.wellnessweb.models.ReservedTherapySession;
import com.example.wellnessweb.models.ServiceResponse;
import com.example.wellnessweb.models.Therapist;
import com.example.wellnessweb.models.TherapistRequest;
import com.example.wellnessweb.models.TherapySession;
import com.example.wellnessweb.repositories.BlogsRepository;
import com.example.wellnessweb.repositories.CustomerRepository;
import com.example.wellnessweb.repositories.TherapistRepository;
import com.example.wellnessweb.repositories.TherapySessionRepository;
import com.example.wellnessweb.repositories.ReservedTherapySessionRepository;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
            if (updatedTherapist.getName() != null && updatedTherapist.getName().length() >= 5) {
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
        if (session.getAttribute("loggedInTherapist") != null) {
            ModelAndView mav = new ModelAndView("therapistDash.html");
            Therapist loggedInTherapist = (Therapist) session.getAttribute("loggedInTherapist");
            TherapistRequest therapistRequest = (TherapistRequest) session.getAttribute("therapistReq");
            LocalDate currentDate = LocalDate.now();
            LocalTime currentTime = LocalTime.now();

            TherapySession upcomingSessions = this.therapySessionRepository
                .findFirstByTherapistIDAndStatusAndDateAfterAndStartTimeAfterOrderByDateAscStartTimeAsc(
                        loggedInTherapist.getID(), "RESERVED", currentDate, currentTime);

            mav.addObject("upcomingSessions", upcomingSessions);
            mav.addObject("therapistReq", therapistRequest);
            mav.addObject("therapist", loggedInTherapist);
            return mav;
        }
        return new ModelAndView("redirect:/employeelogin");
    }

    @GetMapping("viewsessions")
    public ModelAndView getTherapySessions(HttpSession session) {
        ModelAndView mav = new ModelAndView("sessionsTherapistDash.html");
        Therapist loggedInTherapist = (Therapist) session.getAttribute("loggedInTherapist");
        List<TherapySession> therapySessions = this.therapySessionRepository
                .findByTherapistID(loggedInTherapist.getID());
                
        mav.addObject("therapySessions", therapySessions);
        mav.addObject("therapist", loggedInTherapist);
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
    public ModelAndView editClientForm(@RequestParam("id") int sessionId, HttpSession session) {
        ModelAndView mav = new ModelAndView("viewSessionDetailsTherapistDash.html");
        ReservedTherapySession reservedTherapySession = this.reservedTherapySessionRepository
                .findByTherapySessionID(sessionId);
        Therapist loggedInTherapist = (Therapist) session.getAttribute("loggedInTherapist");
        if (reservedTherapySession != null) {
            TherapySession therapySession = this.therapySessionRepository.findById(sessionId);
            Customer customer = this.customerRepository.findByID(reservedTherapySession.getCustomerID());
            mav.addObject("therapySess", therapySession);
            mav.addObject("customer", customer);
            mav.addObject("therapist", loggedInTherapist);
        } else {
            mav.setViewName("redirect:/therapistdashboard/viewsessions");
        }
        return mav;
    }

    @PostMapping("/deleteTherapySession/{id}")
    public ModelAndView deleteTherapySession(@PathVariable("id") int id, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        Therapist loggedInTherapist = (Therapist) session.getAttribute("loggedInTherapist");
        modelAndView.addObject("therapist", loggedInTherapist);
        this.therapySessionRepository.deleteById(id);
        modelAndView.setViewName("redirect:/therapistdashboard/viewsessions?DeleteSuccessful");
        modelAndView.addObject("therapist", loggedInTherapist);
        return modelAndView;
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
        ModelAndView mav = new ModelAndView("updateAccountTherapistDash.html");
        Therapist loggedInTherapist = (Therapist) session.getAttribute("loggedInTherapist");
        Boolean valid = updateAccountFields(therapist, loggedInTherapist);
        String imageName = handleImageUpload(imageFile, loggedInTherapist.getPhoneNumber());
        System.out.println("old pass: " + loggedInTherapist.getPassword());
        if (imageName != null) {
            loggedInTherapist.setImage(imageName);
        }
        if (valid) {
            this.therapistRepository.save(loggedInTherapist);
            session.setAttribute("loggedInTherapist", loggedInTherapist);
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
    public ResponseEntity<Object> changePassword(@RequestBody Map<String, String> passwordMap,
            HttpSession session) throws JsonProcessingException {
        String oldPassword = passwordMap.get("oldPassword");
        String newPassword = passwordMap.get("newPassword");
        System.out.println(oldPassword);
        Therapist loggedInTherapist = (Therapist) session.getAttribute("loggedInTherapist");
        ServiceResponse<String> response;
        if (BCrypt.checkpw(oldPassword, loggedInTherapist.getPassword())) {
            String encoddedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt(12));
            loggedInTherapist.setPassword(encoddedPassword);
            session.setAttribute("loggedInTherapist", loggedInTherapist);
            this.therapistRepository.save(loggedInTherapist);
            response = new ServiceResponse<String>("success", "/therapistdashboard/editaccount");
        } else {
            response = new ServiceResponse<String>("error", "Invalid Old Password");
        }
        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }

}
