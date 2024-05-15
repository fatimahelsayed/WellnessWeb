package com.example.wellnessweb.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Blogs implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;
    private LocalDate Date;
    private LocalTime Time;
    private String ContentofBlog;
    private String TitleofBlog;
    private String IntroofBlog;
    private String illnessName;



    private int userID;
    public Blogs(int iD, int userID, LocalDate date, LocalTime time, String contentofBlog, String titleofBlog,
            String introofBlog, String illnessName) {
        ID = iD;
        this.userID = userID;
        Date = date;
        Time = time;
        ContentofBlog = contentofBlog;
        TitleofBlog = titleofBlog;
        IntroofBlog = introofBlog;
        this.illnessName = illnessName;
    }


    public int getUserID() {
        return userID;
    }


    public void setUserID(int userID) {
        this.userID = userID;
    }





    public String getIllnessName() {
        return illnessName;
    }


    public void setIllnessName(String illnessName) {
        this.illnessName = illnessName;
    }


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


    public LocalTime getTime() {
        return Time;
    }


    public void setTime(LocalTime time) {
        Time = time;
    }





    public String getContentofBlog() {
        return ContentofBlog;
    }


    public void setContentofBlog(String contentofBlog) {
        ContentofBlog = contentofBlog;
    }


    public String getTitleofBlog() {
        return TitleofBlog;
    }


    public void setTitleofBlog(String titleofBlog) {
        TitleofBlog = titleofBlog;
    }


    public String getIntroofBlog() {
        return IntroofBlog;
    }


    public void setIntroofBlog(String introofBlog) {
        IntroofBlog = introofBlog;
    }


   


    public Blogs() {
    }



}
