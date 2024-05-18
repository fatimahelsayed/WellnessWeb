package com.example.wellnessweb.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.wellnessweb.models.Blogs;
import com.example.wellnessweb.models.Content;

public interface ContentRepository extends JpaRepository<Content,Integer> {
    List<Content> findAllByTherapistID(int TherapistID);
    List<Content> findTop4ByOrderByDateDesc();
    List<Content> findAllByIllnessName(String illnessName);



}
