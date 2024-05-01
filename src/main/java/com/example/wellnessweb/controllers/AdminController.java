package com.example.wellnessweb.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.wellnessweb.models.Customer;
import com.example.wellnessweb.models.Therapist;
import com.example.wellnessweb.models.TherapistRequest;
import com.example.wellnessweb.repositories.BlogsRepository;
import com.example.wellnessweb.repositories.CustomerRepository;
import com.example.wellnessweb.repositories.TherapistRepository;
import com.example.wellnessweb.repositories.TherapistRequestRepository;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/admindashboard")
public class AdminController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BlogsRepository blogsRepository;

    @Autowired
    private TherapistRepository therapistRepository;

    @Autowired
    TherapistRequestRepository therapistRequestRepository;

    @GetMapping("")
    public ModelAndView getAdminDashboard() {
        ModelAndView mav = new ModelAndView("admindashboard.html");

        List<Customer> recentCustomers = this.customerRepository.findTop5ByOrderByCreatedAtDesc();
        mav.addObject("recentCustomers", recentCustomers);

        List<TherapistRequest> recentRequests = this.therapistRequestRepository.findTop5ByOrderByCreatedAtDesc();
        mav.addObject("recentRequests", recentRequests);

        List<Therapist> recentTherapists = this.therapistRepository.findTop5ByOrderByCreatedAtDesc();
        mav.addObject("recentTherapists", recentTherapists);

        long blogCount = this.blogsRepository.count();
        long customerCount = this.customerRepository.count();
        long therapistCount = this.therapistRepository.count();

        mav.addObject("blogCount", blogCount);
        mav.addObject("customerCount", customerCount);
        mav.addObject("therapistCount", therapistCount);

        return mav;
    }

    @GetMapping("/therapistsrequests")
    public ModelAndView getTherapistRequests() {
        ModelAndView mav = new ModelAndView("viewTherapistsRequests.html");

        List<TherapistRequest> requests = this.therapistRequestRepository.findAll();
        mav.addObject("requests", requests);

        return mav;
    }

    @GetMapping("/downloadResume/{phoneNumber}")
    public ResponseEntity<Resource> downloadResume(@PathVariable("phoneNumber") String phoneNumber) {
        String resumeFileName = phoneNumber + ".pdf";
        String resumeFilePath = "static/cv/" + resumeFileName;

        Resource resource = new ClassPathResource(resumeFilePath);

        try {
            byte[] data = resource.getInputStream().readAllBytes();
            ByteArrayResource byteArrayResource = new ByteArrayResource(data);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resumeFileName + "\"")
                    .body(byteArrayResource);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/downloadImage/{phoneNumber}")
    public ResponseEntity<Resource> downloadImage(@PathVariable("phoneNumber") String phoneNumber) {
        String[] imageExtensions = { ".jpg", ".jpeg", ".png", ".gif" };

        for (String extension : imageExtensions) {
            String imageFileName = phoneNumber + extension;
            String imageFilePath = "static/images/" + imageFileName;

            Resource resource = new ClassPathResource(imageFilePath);

            try {
                if (resource.exists()) {
                    byte[] data = resource.getInputStream().readAllBytes();
                    ByteArrayResource byteArrayResource = new ByteArrayResource(data);

                    MediaType mediaType = determineMediaType(extension);

                    return ResponseEntity.ok()
                            .contentType(mediaType)
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + imageFileName + "\"")
                            .body(byteArrayResource);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/customers")
    public ModelAndView getCustomers() {
        ModelAndView mav = new ModelAndView("viewCustomers.html");

        List<Customer> customers = this.customerRepository.findAll();
        mav.addObject("customers", customers);

        return mav;
    }

    @GetMapping("/therapists")
    public ModelAndView getTherapists() {
        ModelAndView mav = new ModelAndView("viewTherapistsAdminDash.html");

        List<Therapist> therapists = this.therapistRepository.findAll();
        mav.addObject("therapists", therapists);

        return mav;
    }

    @GetMapping("/therapistrequestdetails/{id}")
    public ModelAndView viewTherapistRequestDetails(@PathVariable("id") int id) {
        ModelAndView modelAndView = new ModelAndView("therapistRequestDetails.html");
        TherapistRequest request = this.therapistRequestRepository.findById(id);
        modelAndView.addObject("request", request);
        return modelAndView;
    }

    private MediaType determineMediaType(String extension) {
        if (extension.equals(".jpg") || extension.equals(".jpeg")) {
            return MediaType.IMAGE_JPEG;
        } else if (extension.equals(".png")) {
            return MediaType.IMAGE_PNG;
        } else if (extension.equals(".gif")) {
            return MediaType.IMAGE_GIF;
        } else {
            return MediaType.IMAGE_JPEG;
        }
    }

}
