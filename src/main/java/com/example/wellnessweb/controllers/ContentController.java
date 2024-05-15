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
import com.example.wellnessweb.repositories.SubtopicRepository;
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
    private SubtopicRepository subtopicRepository;

    @Autowired
    private TherapistRepository therapistRepository;


    @GetMapping("addContent")
    public ModelAndView getArticleForm(HttpSession session) {
        ModelAndView mav = new ModelAndView("addContent.html");
        Therapist loggedInTherapist = (Therapist) session.getAttribute("loggedInTherapist");
       
        mav.addObject("illnesses", illnessRepository.findAll());
        Content articleObj = new Content();

        articleObj.setTherapistID(loggedInTherapist.getID()); 
        mav.addObject("articleObj", articleObj);
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
                                     @RequestParam(value = "subtopicTitle", required = false) List<String> subtopicTitle,
                                     @RequestParam(value = "subtopicContent", required = false) List<String> subtopicContent) {
        try {
            articleObj.setIllnessName(illnessName);
            articleObj.setDate(LocalDate.now());
    
            if (!file.isEmpty()) {
                articleObj.setImage(file.getBytes());
            }
    
            Therapist loggedInTherapist = (Therapist) session.getAttribute("loggedInTherapist");
            articleObj.setTherapistID(loggedInTherapist.getID());
            Content savedArticle = contentRepository.save(articleObj);
            
            // Ensure that the ID of the saved content object is not null
            if (savedArticle.getID() == 0) {
                // Log or handle the situation where the ID is null
                System.out.println("Error: Saved content object ID is null.");
                return new ModelAndView("error.html");
            }
    
            System.out.println("Saved content object ID: " + savedArticle.getID());
             
            if (savedArticle != null && subtopicTitle != null && subtopicContent != null && !subtopicTitle.isEmpty() && !subtopicContent.isEmpty()) {
                List<Subtopics> subtopics = new ArrayList<>();
                for (int i = 0; i < subtopicTitle.size(); i++) {
                    Subtopics subtopic = new Subtopics();
                    subtopic.setSubtopicTitle(subtopicTitle.get(i));
                    subtopic.setSubtopicContent(subtopicContent.get(i));
                    
                    // Set the parent ID of all subtopics to the ID of the saved Content object
                    subtopic.setParentId(savedArticle.getID());
                    
                    // Log the assigned parent ID of each subtopic
                    System.out.println("Subtopic " + (i+1) + " parent ID: " + subtopic.getParentId());
                    
                    subtopics.add(subtopic);
                }
                
                // Set the list of subtopics to the Content object
                savedArticle.setSubtopicList(subtopics);
                
                // Save all subtopics
                subtopicRepository.saveAll(subtopics);
            }
    
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
