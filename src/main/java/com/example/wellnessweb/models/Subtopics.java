
package com.example.wellnessweb.models;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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

    private String subtopicTitle;
    @Column(columnDefinition = "LONGTEXT")
    @Lob
    private String subtopicContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    private Content content;

    public Subtopics(int iD, String subtopicTitle, String subtopicContent, Content content) {
        ID = iD;
        this.subtopicTitle = subtopicTitle;
        this.subtopicContent = subtopicContent;
        this.content = content;
    }

    public Subtopics() {

    }

    public int getID() {
        return ID;
    }

    public void setID(int iD) {
        ID = iD;
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

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

}
