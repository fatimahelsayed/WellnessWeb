package com.example.wellnessweb.models;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;

@Entity
public class Therapist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;
    private int TherapistRequestID;
    private String Name;
    private int Age;
    private String Gender;
    private String PhoneNumber;
    private String Specialization;
    private String Email;
    private String Password;
    private LocalDate createdAt;

    public Therapist() {
    }

    public Therapist(int ID, int TherapistRequestID, String Name, int Age, String Gender, String PhoneNumber, String Specialization, String Email, String Password, LocalDate createdAt) {
        this.ID = ID;
        this.TherapistRequestID = TherapistRequestID;
        this.Name = Name;
        this.Age = Age;
        this.Gender = Gender;
        this.PhoneNumber = PhoneNumber;
        this.Specialization = Specialization;
        this.Email = Email;
        this.Password = Password;
        this.createdAt = createdAt;
    }

    public int getID() {
        return this.ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getTherapistRequestID() {
        return this.TherapistRequestID;
    }

    public void setTherapistRequestID(int TherapistRequestID) {
        this.TherapistRequestID = TherapistRequestID;
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
        return this.PhoneNumber;
    }

    public void setPhoneNumber(String PhoneNumber) {
        this.PhoneNumber = PhoneNumber;
    }

    public String getSpecialization() {
        return this.Specialization;
    }

    public void setSpecialization(String Specialization) {
        this.Specialization = Specialization;
    }

    public String getEmail() {
        return this.Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getPassword() {
        return this.Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
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
