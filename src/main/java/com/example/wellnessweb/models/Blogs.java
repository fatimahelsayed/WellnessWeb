package com.example.wellnessweb.models;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Blogs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;
    private int UserID;
    private LocalDate Date;
    private LocalTime Time;
    private String Info;
    private String ContentofBlog;
    private String TitleofBlog;
    private String IntroofBlog;


    public int getID() {
        return ID;
    }


    public void setID(int iD) {
        ID = iD;
    }


    public int getUserID() {
        return UserID;
    }


    public void setUserID(int userID) {
        UserID = userID;
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


    public String getInfo() {
        return Info;
    }


    public void setInfo(String info) {
        Info = info;
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


    public Blogs(int iD, int userID, LocalDate date, LocalTime time, String info, String contentofBlog,
            String titleofBlog, String introofBlog) {
        ID = iD;
        UserID = userID;
        Date = date;
        Time = time;
        Info = info;
        ContentofBlog = contentofBlog;
        TitleofBlog = titleofBlog;
        IntroofBlog = introofBlog;
    }


    public Blogs() {
    }



}
