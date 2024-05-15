package com.example.wellnessweb.controllers;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.example.wellnessweb.models.Admin;
import com.example.wellnessweb.models.Content;
import com.example.wellnessweb.models.Customer;
import com.example.wellnessweb.models.ReservedTherapySession;
import com.example.wellnessweb.models.ServiceResponse;
import com.example.wellnessweb.models.Therapist;
import com.example.wellnessweb.models.TherapistRequest;
import com.example.wellnessweb.models.TherapySession;
import com.example.wellnessweb.repositories.AdminRepository;
import com.example.wellnessweb.repositories.ContentRepository;

import com.example.wellnessweb.repositories.CustomerRepository;
import com.example.wellnessweb.repositories.ReservedTherapySessionRepository;
import com.example.wellnessweb.repositories.TherapistRepository;
import com.example.wellnessweb.repositories.TherapistRequestRepository;
import com.example.wellnessweb.repositories.TherapySessionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("")
public class IndexController {
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TherapistRequestRepository therapistRequestRepository;

    @Autowired
    private TherapistRepository therapistRepository;

    @Autowired
    private TherapySessionRepository therapySessionRepository;

    @Autowired
    private ReservedTherapySessionRepository reservedTherapySessionRepository;

    @Autowired
    private ContentRepository contentRepository;

    @GetMapping("/home")
    public ModelAndView getHome() {
        ModelAndView mav = new ModelAndView("home.html");

            List<Content> recentArticles = contentRepository.findTop4ByOrderByDateDesc();
            mav.addObject("recentArticles", recentArticles);
            return mav;
        
    }



    @GetMapping("/login")
    public ModelAndView getLogin() {
        ModelAndView mav = new ModelAndView("login.html");
        return mav;
    }

    @GetMapping("/employeelogin")
    public ModelAndView getEmpLogin() {
        ModelAndView mav = new ModelAndView("empLogin.html");
        return mav;
    }

    @PostMapping("/employeelogin")
    public RedirectView EmpLogin(@RequestParam("email") String email,
            @RequestParam("Password") String password,
            HttpSession session) {
        if (this.adminRepository.existsByEmail(email)) {
            Admin dbAdmin = this.adminRepository.findByEmail(email);
            Boolean isPasswordMatched = BCrypt.checkpw(password, dbAdmin.getPassword());
            if (isPasswordMatched) {
                session.setAttribute("loggedInEmp", dbAdmin);
                return new RedirectView("/admindashboard");
            }
        } else {
            if (this.therapistRepository.existsByEmail(email)) {
                Therapist dbTherapist = this.therapistRepository.findByEmail(email);
                Boolean isPasswordMatched = BCrypt.checkpw(password, dbTherapist.getPassword());
                if (isPasswordMatched) {
                    TherapistRequest dbTherapistRequest = this.therapistRequestRepository
                            .findById(dbTherapist.getTherapistRequestID());
                    session.setAttribute("therapistReq", dbTherapistRequest);
                    session.setAttribute("loggedInTherapist", dbTherapist);
                    return new RedirectView("/therapistdashboard");
                }
            }
        }

        return new RedirectView("/employeelogin?error=userNotFound");
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
    public ResponseEntity<Object> saveCustomer(@RequestBody String customerJSON, HttpSession session)
            throws JsonMappingException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Customer customer = objectMapper.readValue(customerJSON, Customer.class);
        boolean emailExists = customerRepository.existsByEmail(customer.getEmail());
        boolean phoneExists = customerRepository.existsByPhoneNumber(customer.getPhoneNumber());
        boolean usernameExists = customerRepository.existsByUsername(customer.getUsername());
        List<String> errors = new ArrayList<>();

        if (emailExists) {
            errors.add("EmailExists");
        }
        if (phoneExists) {
            errors.add("PhoneNumberExists");
        }
        if (usernameExists) {
            errors.add("UsernameTaken");

        } else {
            String encoddedPassword = BCrypt.hashpw(customer.getPassword(), BCrypt.gensalt(12));
            customer.setPassword(encoddedPassword);
            this.customerRepository.save(customer);
            session.setAttribute("loggedInUser", customer);
            // modelAndView.setViewName("redirect:/profile");
            ServiceResponse<String> response = new ServiceResponse<String>("success", "/profile");
            return new ResponseEntity<Object>(response, HttpStatus.OK);
        }
        if (errors.size() > 0) {
            ServiceResponse<List<String>> response = new ServiceResponse<List<String>>("error", errors);
            return new ResponseEntity<Object>(response, HttpStatus.OK);
        }
        return null;
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
            @RequestParam("cvFile") MultipartFile cvFile,
            @RequestParam("imageFile") MultipartFile imageFile) {

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

            String fileName = handleFileUpload(cvFile, request.getPhoneNumber());
            String imageName = handleImageUpload(imageFile, request.getPhoneNumber());

            if (fileName != null) {
                request.setResume(fileName);
            }

            if (imageName != null) {
                request.setImage(imageName);
            }

            this.therapistRequestRepository.save(request);
            modelAndView.setViewName("redirect:/therapistapply?RequestSent");
        } catch (Exception e) {
            System.out.println("Error adding class: " + e.getMessage());
            modelAndView.setViewName("error_page");
        }

        return modelAndView;
    }

    private String handleFileUpload(MultipartFile file, String phoneNumber) {
        String fileName = null;
        try {
            if (!file.isEmpty()) {
                String originalFilename = file.getOriginalFilename();
                String fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.')).toLowerCase();
                String newFileName = phoneNumber + fileExtension;
                String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/cv/";
                String filePath = uploadDir + newFileName;

                // Check if the file is a PDF
                if (fileExtension.equals(".pdf")) {
                    File destFile = new File(filePath);
                    file.transferTo(destFile);
                    fileName = newFileName;
                } else {
                    System.out.println("Invalid file format: " + fileExtension);
                }
            }
        } catch (Exception e) {
            System.out.println("Error storing file: " + e.getMessage());
        }
        return fileName;
    }

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

    @GetMapping("/therapists")
    public ModelAndView getTherapists() {
        ModelAndView mav = new ModelAndView("viewtherapists.html");
        List<Therapist> therapists = this.therapistRepository.findAll();
        List<TherapistRequest> requests = new ArrayList<>();
        for (Therapist therapist : therapists) {
            int requestId = therapist.getTherapistRequestID();
            TherapistRequest therapistInfo = this.therapistRequestRepository.findById(requestId);
            requests.add(therapistInfo);
        }
        mav.addObject("therapists", therapists);
        mav.addObject("requests", requests);

        return mav;
    }

    @GetMapping("/therapist/booktherapysession")
    public ModelAndView getBookTherapySession(@RequestParam("therapistId") int therapistId) {
        Therapist therapist = therapistRepository.findByID(therapistId);

        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        List<TherapySession> therapysessions = this.therapySessionRepository
                .findByTherapistIDAndStatusAndDateAfterAndStartTimeAfterOrderByDateAscStartTimeAsc(
                        therapistId, "UNRESERVED", currentDate, currentTime);

        ModelAndView mav = new ModelAndView("booktherapysession");
        mav.addObject("therapist", therapist);
        mav.addObject("therapysessions", therapysessions);
        return mav;
    }

    @PostMapping("/therapist/booktherapysession")
    public ModelAndView bookTherapySession(@RequestParam("sessionId") int sessionId, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        Customer loggedInUser = (Customer) session.getAttribute("loggedInUser");

        try {
            if (loggedInUser != null) {
                int customerId = loggedInUser.getID();

                TherapySession therapySession = this.therapySessionRepository.findById(sessionId);
                therapySession.setStatus("RESERVED");
                therapySessionRepository.save(therapySession);

                ReservedTherapySession reservedTherapySession = new ReservedTherapySession();
                reservedTherapySession.setTherapySessionID(sessionId);
                reservedTherapySession.setCustomerID(customerId);

                this.reservedTherapySessionRepository.save(reservedTherapySession);

                modelAndView.setViewName("redirect:/therapists?BookedSuccessfully");
            } else {
                modelAndView.setViewName("redirect:/login");
            }
        } catch (Exception e) {
            System.out.println("Error booking therapy session: " + e.getMessage());
            modelAndView.setViewName("error_page");
        }
        return modelAndView;
    }

    @GetMapping("/blogs")
    public ModelAndView getBlogs() {
        ModelAndView mav = new ModelAndView("blogs.html");
        return mav;
    }

    @GetMapping("/addBlog")
    public ModelAndView getaddBlog() {
        ModelAndView mav = new ModelAndView("addBlog.html");
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

    @GetMapping("/contactus")
    public ModelAndView getContactUs() {
        ModelAndView mav = new ModelAndView("contactus.html");
        return mav;
    }

}
