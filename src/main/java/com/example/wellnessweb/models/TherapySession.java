package com.example.wellnessweb.models;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class TherapySession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;
    private int therapistID;
    private LocalDate Date;
    private LocalTime StartTime;
    private LocalTime EndTime;
    private double Price;
    private String Status;

    public TherapySession() {
    }

    public TherapySession(int ID, int TherapistID, LocalDate Date, LocalTime StartTime, LocalTime EndTime, double Price, String Status) {
        this.ID = ID;
        this.therapistID = TherapistID;
        this.Date = Date;
        this.StartTime = StartTime;
        this.EndTime = EndTime;
        this.Price = Price;
        this.Status = Status;
    }

    public int getID() {
        return this.ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getTherapistID() {
        return this.therapistID;
    }

    public void setTherapistID(int TherapistID) {
        this.therapistID = TherapistID;
    }

    public LocalDate getDate() {
        return this.Date;
    }

    public void setDate(LocalDate Date) {
        this.Date = Date;
    }

    public LocalTime getStartTime() {
        return this.StartTime;
    }

    public void setStartTime(LocalTime StartTime) {
        this.StartTime = StartTime;
    }

    public LocalTime getEndTime() {
        return this.EndTime;
    }

    public void setEndTime(LocalTime EndTime) {
        this.EndTime = EndTime;
    }

    public double getPrice() {
        return this.Price;
    }

    public void setPrice(double Price) {
        this.Price = Price;
    }
    public String getStatus() {
        return this.Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }
}
