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

    public Blogs() {
    }

    public Blogs(int ID, int UserID, LocalDate Date, LocalTime Time, String Info) {
        this.ID = ID;
        this.UserID = UserID;
        this.Date = Date;
        this.Time = Time;
        this.Info = Info;
    }

    public int getID() {
        return this.ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getUserID() {
        return this.UserID;
    }

    public void setUserID(int UserID) {
        this.UserID = UserID;
    }

    public LocalDate getDate() {
        return this.Date;
    }

    public void setDate(LocalDate Date) {
        this.Date = Date;
    }

    public LocalTime getTime() {
        return this.Time;
    }

    public void setTime(LocalTime Time) {
        this.Time = Time;
    }

    public String getInfo() {
        return this.Info;
    }

    public void setInfo(String Info) {
        this.Info = Info;
    }

}
