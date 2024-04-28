package com.example.wellnessweb.models;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;
    private int IllnessID;
    private int TherapistID;
    private LocalDate Date;
    private String Type;
    private String Info;

    public Content() {
    }

    public Content(int ID, int IllnessID, LocalDate Date, String Type, String Info) {
        this.ID = ID;
        this.IllnessID = IllnessID;
        this.Date = Date;
        this.Type = Type;
        this.Info = Info;
    }

    public int getID() {
        return this.ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getIllnessID() {
        return this.IllnessID;
    }

    public void setIllnessID(int IllnessID) {
        this.IllnessID = IllnessID;
    }

    public int getTherapistID() {
        return this.TherapistID;
    }

    public void setTherapistID(int TherapistID) {
        this.TherapistID = TherapistID;
    }

    public LocalDate getDate() {
        return this.Date;
    }

    public void setDate(LocalDate Date) {
        this.Date = Date;
    }

    public String getType() {
        return this.Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public String getInfo() {
        return this.Info;
    }

    public void setInfo(String Info) {
        this.Info = Info;
    }

}
