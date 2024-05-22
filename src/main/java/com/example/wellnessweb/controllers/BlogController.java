package com.example.wellnessweb.controllers;

import java.time.LocalDate;

import java.util.List; // Import the List class
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.wellnessweb.models.Blogs;
import com.example.wellnessweb.models.Customer;
import com.example.wellnessweb.repositories.BlogsRepository;
import com.example.wellnessweb.repositories.CustomerRepository;
import com.example.wellnessweb.repositories.IllnessRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping("/blogs")
public class BlogController {

    @Autowired
    private IllnessRepository illnessRepository;

    @Autowired
    private BlogsRepository blogsRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("addBlog")
    public ModelAndView getBlogForm(HttpSession session) {
        if (session.getAttribute("loggedInUser") != null) {
            ModelAndView mav = new ModelAndView("addBlog.html");
            Customer loggedInUser = (Customer) session.getAttribute("loggedInUser");
            mav.addObject("illnesses", illnessRepository.findAll());
            Blogs blogObj = new Blogs();
            blogObj.setUserID(loggedInUser.getID());
            mav.addObject("customer", loggedInUser);
            mav.addObject("blogObj", blogObj);
            return mav;
        }
        return new ModelAndView("redirect:/login");

    }

    @PostMapping("addBlog")
    public ModelAndView saveBlog(@ModelAttribute Blogs blogObj, @RequestParam("illnessName") String illnessName,
            HttpSession session) {
        blogObj.setIllnessName(illnessName);
        blogObj.setDate(LocalDate.now());
        Customer loggedInUser = (Customer) session.getAttribute("loggedInUser");
        blogObj.setUserID(loggedInUser.getID());
        blogsRepository.save(blogObj);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/profile/publishedBlogs");
        return new ModelAndView("redirect:/profile/publishedBlogs");
    }

    @GetMapping("viewblog/{id}")
    public ModelAndView getBlogById(@PathVariable("id") int id) {
        ModelAndView mav = new ModelAndView("blogs.html");
        Blogs blogObj = blogsRepository.findById(id).orElse(null);

        if (blogObj != null) {
            Customer author = customerRepository.findById(blogObj.getUserID()).orElse(null);

            if (author != null) {
                mav.addObject("authorUsername", author.getUsername());
            }
        }

        mav.addObject("blogObj", blogObj);
        return mav;
    }

}
