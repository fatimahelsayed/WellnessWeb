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
    private int CustomerID;
    private int TherapySessionID;

    public ReservedTherapySession() {
    }

    public ReservedTherapySession(int ID, int CustomerID, int TherapySessionID) {
        this.ID = ID;
        this.CustomerID = CustomerID;
        this.TherapySessionID = TherapySessionID;
    }

    public int getID() {
        return this.ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getCustomerID() {
        return this.CustomerID;
    }

    public void setCustomerID(int CustomerID) {
        this.CustomerID = CustomerID;
    }

    public int getTherapySessionID() {
        return this.TherapySessionID;
    }

    public void setTherapySessionID(int TherapySessionID) {
        this.TherapySessionID = TherapySessionID;
    }



    
}
