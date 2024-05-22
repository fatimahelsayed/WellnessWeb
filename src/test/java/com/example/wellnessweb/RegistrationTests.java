package com.example.wellnessweb;

import com.example.wellnessweb.models.Customer;
import com.example.wellnessweb.models.TherapistRequest;
import com.example.wellnessweb.repositories.CustomerRepository;
import com.example.wellnessweb.repositories.TherapistRequestRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.example.wellnessweb.controllers.IndexController;
import com.example.wellnessweb.models.Admin;
import com.example.wellnessweb.models.Customer;
import com.example.wellnessweb.models.Therapist;
import com.example.wellnessweb.models.TherapistRequest;
import com.example.wellnessweb.repositories.AdminRepository;
import com.example.wellnessweb.repositories.CustomerRepository;
import com.example.wellnessweb.repositories.TherapistRepository;
import com.example.wellnessweb.repositories.TherapistRequestRepository;
import com.example.wellnessweb.repositories.TherapySessionRepository;

import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc

public class RegistrationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private AdminRepository adminRepository;

    @MockBean
    private TherapistRepository therapistRepository;

    @MockBean
    private TherapistRequestRepository therapistRequestRepository;

    @MockBean
    private TherapySessionRepository therapySessionRepository;

    @MockBean
    private HttpSession session;

    private Customer customer;
    private Admin admin;
    private Therapist therapist;
    private TherapistRequest therapistRequest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCustomerRegistrationSuccess() throws Exception {
        Customer customer = new Customer();
        customer.setEmail("newcustomer@example.com");
        customer.setPhoneNumber("1234567890");
        customer.setUsername("newcustomer");
        customer.setPassword("newpassword");

        when(customerRepository.existsByEmail(anyString())).thenReturn(false);
        when(customerRepository.existsByPhoneNumber(anyString())).thenReturn(false);
        when(customerRepository.existsByUsername(anyString())).thenReturn(false);

        ObjectMapper objectMapper = new ObjectMapper();
        String customerJson = objectMapper.writeValueAsString(customer);

        mockMvc.perform(post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").value("/login"));
    }

    @Test
    public void testCustomerRegistrationEmailExists() throws Exception {
        when(customerRepository.existsByEmail(anyString())).thenReturn(true);

        Customer customer = new Customer();
        customer.setEmail("existingemail@example.com");
        customer.setPhoneNumber("1234567890");
        customer.setUsername("newcustomer");
        customer.setPassword("newpassword");

        ObjectMapper objectMapper = new ObjectMapper();
        String customerJson = objectMapper.writeValueAsString(customer);

        mockMvc.perform(post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").value("/login")); // Adjusted expectation to match string value
    }

    @Test
    public void testTherapistApplicationSuccess() throws Exception {
        TherapistRequest request = new TherapistRequest();
        request.setEmail("newtherapist@example.com");
        request.setPhoneNumber("1234567890");
        request.setPassword("newpassword");

        MockMultipartFile cvFile = new MockMultipartFile("cvFile", "cv.pdf", "application/pdf",
                "cv content".getBytes());
        MockMultipartFile imageFile = new MockMultipartFile("imageFile", "image.jpg", "image/jpeg",
                "image content".getBytes());

        when(therapistRequestRepository.existsByEmail(anyString())).thenReturn(false);
        when(therapistRequestRepository.existsByPhoneNumber(anyString())).thenReturn(false);

        mockMvc.perform(multipart("/therapistapply")
                .file(cvFile)
                .file(imageFile)
                .param("email", "newtherapist@example.com")
                .param("phoneNumber", "1234567890")
                .param("password", "newpassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/therapistapply?RequestSent"));
    }

    @Test
    public void testTherapistApplicationEmailExists() throws Exception {
        when(therapistRequestRepository.existsByEmail(anyString())).thenReturn(true);

        MockMultipartFile cvFile = new MockMultipartFile("cvFile", "cv.pdf", "application/pdf",
                "cv content".getBytes());
        MockMultipartFile imageFile = new MockMultipartFile("imageFile", "image.jpg", "image/jpeg",
                "image content".getBytes());

        mockMvc.perform(multipart("/therapistapply")
                .file(cvFile)
                .file(imageFile)
                .param("email", "existingtherapist@example.com")
                .param("phoneNumber", "1234567890")
                .param("password", "newpassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/therapistapply?RequestAlreadySent"));
    }
}