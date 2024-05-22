package com.example.wellnessweb;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import com.example.wellnessweb.models.Customer;
import com.example.wellnessweb.models.ReservedTherapySession;
import com.example.wellnessweb.models.TherapySession;
import com.example.wellnessweb.repositories.ReservedTherapySessionRepository;
import com.example.wellnessweb.repositories.TherapySessionRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class BookTherapySessionTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TherapySessionRepository therapySessionRepository;

    @MockBean
    private ReservedTherapySessionRepository reservedTherapySessionRepository;

    private TherapySession session1;
    private Customer loggedInUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        session1 = new TherapySession();
        session1.setID(1);
        session1.setTherapistID(1);
        session1.setStatus("UNRESERVED");
        session1.setDate(LocalDate.now());
        session1.setStartTime(LocalTime.now().plusHours(1));

        loggedInUser = new Customer();
        loggedInUser.setID(1);
        loggedInUser.setName("jana hani");
        loggedInUser.setAge(20);
        loggedInUser.setGender("Female");
        loggedInUser.setPhoneNumber("01091119866");
        loggedInUser.setEmail("janahani.nbis@gmail.com");
        loggedInUser.setUsername("janjoon");
        loggedInUser.setPassword("jana123");
        loggedInUser.setCreatedAt(LocalDate.now());
    }

    @Test
    public void testBookTherapySession() throws Exception {
        // Mock the behavior of repositories
        when(therapySessionRepository.findById(anyInt())).thenReturn(session1);

        // Set up HttpSession to return loggedInUser attribute
        MockHttpSession mockSession = new MockHttpSession();
        mockSession.setAttribute("loggedInUser", loggedInUser);

        System.out.println("Session attribute: " + mockSession.getAttribute("loggedInUser"));

        // Perform the request
        mockMvc.perform(post("/therapist/booktherapysession")
                .param("sessionId", "1")
                .session(mockSession))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/therapists?BookedSuccessfully"));

        // Verify that therapySessionRepository.save() and
        // reservedTherapySessionRepository.save() are called with the correct objects
        verify(therapySessionRepository, times(1)).save(any(TherapySession.class));
        verify(reservedTherapySessionRepository, times(1)).save(any(ReservedTherapySession.class));
    }

    @Test
    public void testBookTherapySessionWhenNotLoggedIn() throws Exception {
        mockMvc.perform(post("/therapist/booktherapysession").param("sessionId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
}
