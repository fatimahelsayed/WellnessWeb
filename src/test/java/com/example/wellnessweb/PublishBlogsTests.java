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
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import com.example.wellnessweb.models.Illness;
import com.example.wellnessweb.models.Blogs;
import com.example.wellnessweb.models.Customer;
import com.example.wellnessweb.repositories.BlogsRepository;
import com.example.wellnessweb.repositories.CustomerRepository;
import com.example.wellnessweb.repositories.IllnessRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class PublishBlogsTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BlogsRepository blogsRepository;

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private IllnessRepository illnessRepository;

    private Customer loggedInUser;

    private List<Illness> illnesses = new ArrayList<>();

    private Illness illness;

    private Blogs blog;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

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

        illnesses = illnessRepository.findAll();

    }

    @Test
    public void testPublishBlog() throws Exception {
        MockHttpSession mockSession = new MockHttpSession();
        mockSession.setAttribute("loggedInUser", loggedInUser);

        System.out.println("Session attribute: " + mockSession.getAttribute("loggedInUser"));

        Blogs mockBlog = new Blogs(
                0, 
                loggedInUser.getID(),
                LocalDate.now(),
                LocalTime.now(),
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer vel orci leo. Aenean lectus neque, viverra eu accumsan vitae, imperdiet non ipsum. Pellentesque ultricies euismod lectus, et imperdiet tellus molestie id. Aliquam sit amet ullamcorper purus. Cras ultrices fringilla vehicula. Curabitur ex quam, accumsan ut orci sit amet, condimentum tristique.",
                "Blog Title",
                "Introduction to the blog.",
                "Depression");

        when(blogsRepository.save(any(Blogs.class))).thenReturn(mockBlog);

        mockMvc.perform(post("/blogs/addBlog")
                .param("illnessName", "Illness Name")
                .session(mockSession)
                .flashAttr("blogObj", mockBlog))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile/publishedBlogs"));
    }

}
