package com.example.wellnessweb.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.wellnessweb.models.Blogs;
import com.example.wellnessweb.models.Content;
import com.example.wellnessweb.models.Customer;
import com.example.wellnessweb.models.Illness;
import com.example.wellnessweb.models.Subtopics;
import com.example.wellnessweb.models.Therapist;
import com.example.wellnessweb.repositories.BlogsRepository;
import com.example.wellnessweb.repositories.IllnessRepository;
import com.example.wellnessweb.repositories.ContentRepository;
import com.example.wellnessweb.repositories.SubtopicsRepository;
import com.example.wellnessweb.repositories.TherapistRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping("/content")
public class ContentController {

    @Autowired
    private IllnessRepository illnessRepository;

    @Autowired
    private ContentRepository contentRepository;
 
    @Autowired
    private SubtopicsRepository subtopicRepository;

    @Autowired
    private TherapistRepository therapistRepository;


    @GetMapping("addContent")
    public ModelAndView getArticleForm(HttpSession session) {
        ModelAndView mav = new ModelAndView("addContent.html");
        Therapist loggedInTherapist = (Therapist) session.getAttribute("loggedInTherapist");
        mav.addObject("illnesses", illnessRepository.findAll());
        Content articleObj = new Content();
        
        List<Subtopics> subtopicsList = new ArrayList<>();
        subtopicsList.add(new Subtopics());
            articleObj.setSubtopicList(subtopicsList);
    
        articleObj.setTherapistID(loggedInTherapist.getID()); 
        mav.addObject("articleObj", articleObj);
        mav.addObject("subtopicsList", subtopicsList);
    
        mav.addObject("therapist", loggedInTherapist);
    
        return mav;
    }
    
    public static String convertToBase64(byte[] imageData) {
        return Base64.getEncoder().encodeToString(imageData);
    }


    @GetMapping("viewContent/{id}")
    public ModelAndView getBlogById(@PathVariable("id") int id) {
        ModelAndView mav = new ModelAndView("content.html");
        Content contentObj = contentRepository.findById(id).orElse(null);
        if (contentObj != null) {

            Therapist author = therapistRepository.findById(contentObj.getTherapistID()).orElse(null);
            byte[] image = contentObj.getImage();
            String base64Image = (image != null) ? Base64.getEncoder().encodeToString(image) : "";
            mav.addObject("contentObj", contentObj);
            mav.addObject("base64Image", base64Image);

            if (author != null) {
                mav.addObject("authorName", author.getName());
            }
        }
        return mav;
    }
    

    @Transactional
    @PostMapping("addContent")
    public ModelAndView saveArticle(HttpSession session, @ModelAttribute Content articleObj,
                                     @RequestParam("illnessName") String illnessName,
                                     @RequestParam("file") MultipartFile file,
                                     @RequestParam("subtopicTitles") List<String> subtopicTitles,
                                     @RequestParam("subtopicContents") List<String> subtopicContents) {
        try {
            articleObj.setIllnessName(illnessName);
            articleObj.setDate(LocalDate.now());
    
            if (!file.isEmpty()) {
                articleObj.setImage(file.getBytes());
            }
    
            Therapist loggedInTherapist = (Therapist) session.getAttribute("loggedInTherapist");
            articleObj.setTherapistID(loggedInTherapist.getID());
    
            List<Subtopics> subtopics = new ArrayList<>();
            for (int i = 0; i < subtopicTitles.size(); i++) {
                Subtopics subtopic = new Subtopics();
                subtopic.setSubtopicTitle(subtopicTitles.get(i));
                subtopic.setSubtopicContent(subtopicContents.get(i));
                subtopic.setContent(articleObj);
                subtopics.add(subtopic);
            }
            articleObj.setSubtopicList(subtopics);
    
            contentRepository.save(articleObj);
            
            return new ModelAndView("redirect:/home");
        } catch (IOException e) {
            e.printStackTrace();
            return new ModelAndView("error.html");
        }
    }
    

    @GetMapping("/{illnessName}")
    public ModelAndView getContentByIllnessName(@PathVariable String illnessName) {
        ModelAndView mav = new ModelAndView("illnesses.html");
        List<Content> contentList = contentRepository.findAllByIllnessName(illnessName);
        System.out.println("Content List: " + contentList); // Debugging statement
        mav.addObject("contentList", contentList);

        Illness illness = illnessRepository.findByName(illnessName);
        mav.addObject("illnessesDesc", illness.getDescription());
        mav.addObject("illnessesSymptoms", illness.getSymptoms());

       
        return mav;
    }
    
     
}
