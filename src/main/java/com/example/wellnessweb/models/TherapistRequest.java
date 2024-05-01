package com.example.wellnessweb.models;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;

@Entity
public class TherapistRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;
    private String Name;
    private int Age;
    private String Gender;
    private String phoneNumber;
    private String Specialization;
    private String email;
    private String Password;
    private String Image;
    private String Resume;
    private String isAccepted;
    private LocalDate createdAt;

    public TherapistRequest() {
    }

    public TherapistRequest(int ID, String Name, int Age, String Gender, String phoneNumber, String Specialization, String email, String Password, String Image, String Resume, String isAccepted, LocalDate createdAt) {
        this.ID = ID;
        this.Name = Name;
        this.Age = Age;
        this.Gender = Gender;
        this.phoneNumber = phoneNumber;
        this.Specialization = Specialization;
        this.email = email;
        this.Password = Password;
        this.Image = Image;
        this.Resume = Resume;
        this.isAccepted = isAccepted;
        this.createdAt = createdAt;
    }

    public int getID() {
        return this.ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return this.Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public int getAge() {
        return this.Age;
    }

    public void setAge(int Age) {
        this.Age = Age;
    }

    public String getGender() {
        return this.Gender;
    }

    public void setGender(String Gender) {
        this.Gender = Gender;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSpecialization() {
        return this.Specialization;
    }

    public void setSpecialization(String Specialization) {
        this.Specialization = Specialization;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public String getImage() {
        return this.Image;
    }

    public void setImage(String Image) {
        this.Image = Image;
    }

    public String getResume() {
        return this.Resume;
    }

    public void setResume(String Resume) {
        this.Resume = Resume;
    }

    public String getIsAccepted() {
        return this.isAccepted;
    }

    public void setIsAccepted(String isAccepted) {
        this.isAccepted = isAccepted;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDate.now(); 
    }

}
