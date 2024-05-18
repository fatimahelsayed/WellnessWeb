package com.example.wellnessweb.controllers;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.wellnessweb.models.Blogs;
import com.example.wellnessweb.models.Customer;
import com.example.wellnessweb.models.ReservedTherapySession;
import com.example.wellnessweb.models.ServiceResponse;
import com.example.wellnessweb.models.Therapist;
import com.example.wellnessweb.models.TherapistRequest;
import com.example.wellnessweb.models.TherapySession;
import com.example.wellnessweb.repositories.ReservedTherapySessionRepository;
import com.example.wellnessweb.repositories.TherapistRepository;
import com.example.wellnessweb.repositories.TherapySessionRepository;
import com.example.wellnessweb.repositories.CustomerRepository;
import com.example.wellnessweb.repositories.BlogsRepository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import aj.org.objectweb.asm.Attribute;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/profile")
public class UserController {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ReservedTherapySessionRepository reservedTherapySessionRepository;

    @Autowired
    private TherapySessionRepository therapySessionRepository;

    @Autowired
    private TherapistRepository therapistRepository;

    @Autowired
    private BlogsRepository blogsRepository;

    public List<String> checkForExistingFields(Customer customer, Customer loggedInUser) {
        List<String> errors = new ArrayList<>();
        if (!loggedInUser.getEmail().equals(customer.getEmail())) {
            boolean emailExists = customerRepository.existsByEmail(customer.getEmail());
            if (emailExists) {
                errors.add("EmailExists");
            }
        }
        if (!loggedInUser.getUsername().equals(customer.getUsername())) {
            boolean usernameExists = customerRepository.existsByUsername(customer.getUsername());
            if (usernameExists) {
                errors.add("UsernameTaken");
            }
        }
        if (customer.getPhoneNumber() != null && loggedInUser.getPhoneNumber() != null) {
            if (!loggedInUser.getPhoneNumber().equals(customer.getPhoneNumber())) {
                boolean phoneExists = customerRepository.existsByPhoneNumber(customer.getPhoneNumber());
                if (phoneExists) {
                    errors.add("PhoneNumberExists");
                }
            }
        }
        return errors;
    }

    public void updateFields(Customer updatedCustomer, Customer loggedInUser) {
        if (updatedCustomer.getEmail() != null && !loggedInUser.getEmail().equals(updatedCustomer.getEmail())) {
            loggedInUser.setEmail(updatedCustomer.getEmail());
        }
        if (updatedCustomer.getUsername() != null
                && !loggedInUser.getUsername().equals(updatedCustomer.getUsername())) {
            loggedInUser.setUsername(updatedCustomer.getUsername());
        }
        if (updatedCustomer.getPhoneNumber() != null
                && !loggedInUser.getPhoneNumber().equals(updatedCustomer.getPhoneNumber())) {
            loggedInUser.setPhoneNumber(updatedCustomer.getPhoneNumber());

        }
        if (updatedCustomer.getGender() != null && !updatedCustomer.getGender().equals(loggedInUser.getGender())) {
            loggedInUser.setName(updatedCustomer.getName());
        }
        if (updatedCustomer.getAge() != 0 && updatedCustomer.getAge() != loggedInUser.getAge()) {
            loggedInUser.setAge(updatedCustomer.getAge());
        }
        this.customerRepository.save(loggedInUser);
    }

    @GetMapping("")
    public ModelAndView getProfile(HttpSession session) {
        if (session.getAttribute("loggedInUser") != null) {
            Customer loggedInUser = (Customer) session.getAttribute("loggedInUser");
            ModelAndView mav = new ModelAndView("userProfile.html");
            ReservedTherapySession reservedSession = reservedTherapySessionRepository
                    .findFirstByCustomerIDOrderByIDDesc(loggedInUser.getID());
            if (reservedSession != null) {
                TherapySession therapySession = this.therapySessionRepository
                        .findById(reservedSession.getTherapySessionID());
                Therapist therapist = this.therapistRepository.findByID(therapySession.getTherapistID());
                mav.addObject("therapySession", therapySession);
                mav.addObject("therapist", therapist);
            }
            mav.addObject("customer", loggedInUser);
            return mav;
        }
        return new ModelAndView("redirect:/login");
    }

    @GetMapping("editaccount")
    public ModelAndView getUpdateAccountForm(HttpSession session) {
        if (session.getAttribute("loggedInUser") != null) {

            ModelAndView mav = new ModelAndView("userProfileEdit.html");
            Customer loggedInUser = (Customer) session.getAttribute("loggedInUser");
            mav.addObject("customer", loggedInUser);
            return mav;
        }
        return new ModelAndView("redirect:/login");
    }

    @PostMapping("/editaccount")
    public ResponseEntity<Object> updateCustomer(@RequestBody String customerJSON, HttpSession session)
            throws JsonMappingException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Customer customer = objectMapper.readValue(customerJSON, Customer.class);
        Customer loggedInUser = (Customer) session.getAttribute("loggedInUser");

        List<String> errors = checkForExistingFields(customer, loggedInUser);

        Boolean isPasswordMatched = BCrypt.checkpw(customer.getPassword(), loggedInUser.getPassword());
        if (!isPasswordMatched) {
            errors.add("IncorrectPassword");
        }

        if (isPasswordMatched && errors.size() == 0) {
            updateFields(customer, loggedInUser);
            session.setAttribute("loggedInUser", loggedInUser);
            ServiceResponse<String> response = new ServiceResponse<String>("success", "/profile");
            return new ResponseEntity<Object>(response, HttpStatus.OK);
        } else if (errors.size() > 0) {
            ServiceResponse<List<String>> response = new ServiceResponse<List<String>>("error", errors);
            return new ResponseEntity<Object>(response, HttpStatus.OK);
        }
        return null;
    }

    @GetMapping("changepassword")
    public ModelAndView getChangePasswordForm(HttpSession session) {
        if (session.getAttribute("loggedInUser") != null) {
            ModelAndView mav = new ModelAndView("changePasswordUserProfile.html");
            Customer loggedInCustomer = (Customer) session.getAttribute("loggedInUser");
            mav.addObject("customer", loggedInCustomer);
            return mav;
        }
        return new ModelAndView("redirect:/login");

    }

    @PostMapping("changepassword")
    public ResponseEntity<Object> changePassword(@RequestBody Map<String, String> passwordMap,
            HttpSession session) throws JsonProcessingException {
        String oldPassword = passwordMap.get("oldPassword");
        String newPassword = passwordMap.get("newPassword");
        System.out.println(oldPassword);
        Customer loggedInCustomer = (Customer) session.getAttribute("loggedInUser");
        ServiceResponse<String> response;
        if (BCrypt.checkpw(oldPassword, loggedInCustomer.getPassword())) {
            String encoddedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt(12));
            loggedInCustomer.setPassword(encoddedPassword);
            session.setAttribute("loggedInUser", loggedInCustomer);
            this.customerRepository.save(loggedInCustomer);
            response = new ServiceResponse<String>("success", "/profile/editaccount");
        } else {
            response = new ServiceResponse<String>("error", "Invalid Old Password");
        }
        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }

    @GetMapping("/bookedSessions")
    public ModelAndView getBookedSessions(HttpSession session) {
        Customer loggedInUser = (Customer) session.getAttribute("loggedInUser");
        ModelAndView mav = new ModelAndView("userProfileBooked.html");
        ReservedTherapySession reservedSession = this.reservedTherapySessionRepository
                .findByCustomerID(loggedInUser.getID());
        if (reservedSession != null) {
            TherapySession therapySession = this.therapySessionRepository
                    .findById(reservedSession.getTherapySessionID());
            Therapist therapist = this.therapistRepository.findByID(therapySession.getTherapistID());
            mav.addObject("therapist", therapist);
            mav.addObject("therapySession", therapySession);
        }
        mav.addObject("customer", loggedInUser);

        return mav;

    }

    @GetMapping("/publishedBlogs")
    public ModelAndView getUserPublishedBlogs(HttpSession session) {
        ModelAndView mav = new ModelAndView("userPublishedBlogs.html");
        Customer loggedInUser = (Customer) session.getAttribute("loggedInUser");
        List<Blogs> userBlogs = blogsRepository.findAllByUserID(loggedInUser.getID());
        mav.addObject("customer", loggedInUser);
        mav.addObject("userBlogs", userBlogs);
        return mav;
    }

    @GetMapping("/feed")
    public ModelAndView getBlogsFeed(HttpSession session) {
        ModelAndView mav = new ModelAndView("blogsFeed.html");

        Customer loggedInUser = (Customer) session.getAttribute("loggedInUser");
        List<Blogs> latestBlogs = blogsRepository.findTop50ByOrderByDateDescTimeDesc();

        if (loggedInUser == null) {
            return new ModelAndView("redirect:/login");
        }

        mav.addObject("customer", loggedInUser);
        mav.addObject("latestBlogs", latestBlogs);

        return mav;
    }

  
    @PostMapping("/publishedBlogs/delete/{id}")
    public ModelAndView deleteBlog(@PathVariable("id") int blogId) {
        Optional<Blogs> optionalBlog = blogsRepository.findById(blogId);
        if (!optionalBlog.isPresent()) {
            ModelAndView mav = new ModelAndView("publishedBlogs.html");
            mav.addObject("error", "Blog not found");
            return mav;
        }
    
        blogsRepository.deleteById(blogId);
    
        return new ModelAndView("redirect:/profile/publishedBlogs");
    }
    


    @GetMapping("/logout")
    public ModelAndView logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
        return new ModelAndView("redirect:/home");
    }

}
