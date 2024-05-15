package com.example.wellnessweb.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
 
import javax.persistence.Lob;

@Entity
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;
    private int therapistID;


    public Content(int iD, int therapistID, LocalDate date, String illnessName, String contentofArticle,
            String titleofArticle, String introofArticle, String quote, String authorOfQuote,
            List<Subtopics> subtopicList, byte[] image) {
        ID = iD;
        this.therapistID = therapistID;
        Date = date;
        this.illnessName = illnessName;
        ContentofArticle = contentofArticle;
        TitleofArticle = titleofArticle;
        IntroofArticle = introofArticle;
        this.quote = quote;
        this.authorOfQuote = authorOfQuote;
        this.subtopicList = subtopicList;
        this.image = image;
    }

    public int getTherapistID() {
        return therapistID;
    }

    public void setTherapistID(int therapistID) {
        this.therapistID = therapistID;
    }

    private LocalDate Date;
    private String illnessName;
    private String ContentofArticle;
    private String TitleofArticle;
    private String IntroofArticle;
    private String quote;
    private String authorOfQuote;

    @OneToMany(mappedBy = "content", cascade = CascadeType.ALL)
 private List<Subtopics> subtopicList;

   
    public List<Subtopics> getSubtopicList() {
        return subtopicList;
    }
    
    public void setSubtopicList(List<Subtopics> subtopicList) {
        this.subtopicList = subtopicList;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getAuthorOfQuote() {
        return authorOfQuote;
    }

    public void setAuthorOfQuote(String authorOfQuote) {
        this.authorOfQuote = authorOfQuote;
    }

    public Content() {

    }

    @Lob
    private byte[] image;

    public int getID() {
        return ID;
    }

    public void setID(int iD) {
        ID = iD;
    }

    

    public LocalDate getDate() {
        return Date;
    }

    public void setDate(LocalDate date) {
        Date = date;
    }

    public String getIllnessName() {
        return illnessName;
    }

    public void setIllnessName(String illnessName) {
        this.illnessName = illnessName;
    }

    public String getContentofArticle() {
        return ContentofArticle;
    }

    public void setContentofArticle(String contentofArticle) {
        ContentofArticle = contentofArticle;
    }

    public String getTitleofArticle() {
        return TitleofArticle;
    }

    public void setTitleofArticle(String titleofArticle) {
        TitleofArticle = titleofArticle;
    }

    public String getIntroofArticle() {
        return IntroofArticle;
    }

    public void setIntroofArticle(String introofArticle) {
        IntroofArticle = introofArticle;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

}
