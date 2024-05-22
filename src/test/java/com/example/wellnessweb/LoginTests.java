package com.example.wellnessweb;

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
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import java.util.Optional;


@SpringBootTest
@AutoConfigureMockMvc
public class LoginTests {

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
        customer = new Customer();
        customer.setUsername("customerUser");
        customer.setPassword(BCrypt.hashpw("customerPass", BCrypt.gensalt()));

        admin = new Admin();
        admin.setEmail("admin@example.com");
        admin.setPassword(BCrypt.hashpw("adminPass", BCrypt.gensalt()));

        therapist = new Therapist();
        therapist.setEmail("therapist@example.com");
        therapist.setPassword(BCrypt.hashpw("therapistPass", BCrypt.gensalt()));

        therapistRequest = new TherapistRequest();
        therapistRequest.setEmail("therapist@example.com");
        therapistRequest.setPassword(BCrypt.hashpw("therapistPass", BCrypt.gensalt()));
    }

    @Test
    public void testCustomerLoginSuccess() throws Exception {
        when(customerRepository.findByUsername("customerUser")).thenReturn(customer);

        ResultActions result = mockMvc.perform(post("/login")
                .param("username", "customerUser")
                .param("password", "customerPass")
                .sessionAttr("session", session));

        result.andExpect(redirectedUrl("/profile"));
    }

    @Test
    public void testCustomerLoginWrongPassword() throws Exception {
        when(customerRepository.findByUsername("customerUser")).thenReturn(customer);

        ResultActions result = mockMvc.perform(post("/login")
                .param("username", "customerUser")
                .param("password", "wrongPassword")
                .sessionAttr("session", session));

        result.andExpect(redirectedUrl("/login?error=wrongPassword"));
    }

    @Test
    public void testCustomerLoginUserNotFound() throws Exception {
        when(customerRepository.findByUsername("nonexistentUser")).thenReturn(null);

        ResultActions result = mockMvc.perform(post("/login")
                .param("username", "nonexistentUser")
                .param("password", "somePassword")
                .sessionAttr("session", session));

        result.andExpect(redirectedUrl("/login?error=userNotFound"));
    }

    @Test
    public void testAdminLoginSuccess() throws Exception {
        when(adminRepository.existsByEmail("admin@example.com")).thenReturn(true);
        when(adminRepository.findByEmail("admin@example.com")).thenReturn(admin);

        ResultActions result = mockMvc.perform(post("/employeelogin")
                .param("email", "admin@example.com")
                .param("Password", "adminPass")
                .sessionAttr("session", session));

        result.andExpect(redirectedUrl("/admindashboard"));
    }

    @Test
    public void testAdminLoginWrongPassword() throws Exception {
        when(adminRepository.existsByEmail("admin@example.com")).thenReturn(true);
        when(adminRepository.findByEmail("admin@example.com")).thenReturn(admin);

        ResultActions result = mockMvc.perform(post("/employeelogin")
                .param("email", "admin@example.com")
                .param("Password", "wrongPassword")
                .sessionAttr("session", session));

        result.andExpect(redirectedUrl("/employeelogin?error=userNotFound"));
    }

    @Test
    public void testTherapistLoginSuccess() throws Exception {
        when(therapistRepository.existsByEmail("therapist@example.com")).thenReturn(true);
        when(therapistRepository.findByEmail("therapist@example.com")).thenReturn(therapist);
        when(therapistRequestRepository.findById(therapist.getTherapistRequestID())).thenReturn(therapistRequest);

        ResultActions result = mockMvc.perform(post("/employeelogin")
                .param("email", "therapist@example.com")
                .param("Password", "therapistPass")
                .sessionAttr("session", session));

        result.andExpect(redirectedUrl("/therapistdashboard"));
    }

    @Test
    public void testTherapistLoginWrongPassword() throws Exception {
        when(therapistRepository.existsByEmail("therapist@example.com")).thenReturn(true);
        when(therapistRepository.findByEmail("therapist@example.com")).thenReturn(therapist);

        ResultActions result = mockMvc.perform(post("/employeelogin")
                .param("email", "therapist@example.com")
                .param("Password", "wrongPassword")
                .sessionAttr("session", session));

        result.andExpect(redirectedUrl("/employeelogin?error=userNotFound"));
    }

    @Test
    public void testEmployeeLoginUserNotFound() throws Exception {
        when(adminRepository.existsByEmail("nonexistent@example.com")).thenReturn(false);
        when(therapistRepository.existsByEmail("nonexistent@example.com")).thenReturn(false);

        ResultActions result = mockMvc.perform(post("/employeelogin")
                .param("email", "nonexistent@example.com")
                .param("Password", "somePassword")
                .sessionAttr("session", session));

        result.andExpect(redirectedUrl("/employeelogin?error=userNotFound"));
    }
}
