package com.example.wellnessweb.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ReservedTherapySession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;
    private int customerID;
    private int therapySessionID;

    public ReservedTherapySession() {
    }

    public ReservedTherapySession(int ID, int customerID, int TherapySessionID) {
        this.ID = ID;
        this.customerID = customerID;
        this.therapySessionID = TherapySessionID;
    }

    public int getID() {
        return this.ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getCustomerID() {
        return this.customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getTherapySessionID() {
        return this.therapySessionID;
    }

    public void setTherapySessionID(int TherapySessionID) {
        this.therapySessionID = TherapySessionID;
    }



    
}
