package com.example.wellnessweb.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.wellnessweb.models.Illness;
import com.example.wellnessweb.models.Subtopics;

public interface SubtopicsRepository extends JpaRepository<Subtopics,Integer> {
        List<Subtopics> findAll();

}
