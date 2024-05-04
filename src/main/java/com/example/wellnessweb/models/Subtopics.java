
package com.example.wellnessweb.models;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import javax.persistence.Lob;

@Entity
public class Subtopics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;

    @Column(name = "parent_id", insertable = false, updatable = false)
    private int parentId;

    private String subtopicTitle;
    private String subtopicContent;

    @ManyToOne
    @JoinColumn(name = "parent_id") // Adjust the column name as per your database schema
    private Content content;

    public Subtopics(){

    }

    public int getID() {
        return ID;
    }
    public void setID(int iD) {
        ID = iD;
    }
    public int getParentId() {
        return parentId;
    }
    public void setParentId(int parentId) {
        this.parentId = parentId;
    }
    public String getSubtopicTitle() {
        return subtopicTitle;
    }
    public void setSubtopicTitle(String subtopicTitle) {
        this.subtopicTitle = subtopicTitle;
    }
    public String getSubtopicContent() {
        return subtopicContent;
    }
    public void setSubtopicContent(String subtopicContent) {
        this.subtopicContent = subtopicContent;
    }
    public Subtopics(int iD, int parentId, String subtopicTitle, String subtopicContent) {
        ID = iD;
        this.parentId = parentId;
        this.subtopicTitle = subtopicTitle;
        this.subtopicContent = subtopicContent;
    }


}
