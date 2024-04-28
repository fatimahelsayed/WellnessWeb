package com.example.wellnessweb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.wellnessweb.models.TherapySession;

public interface TherapySessionRepository extends JpaRepository<TherapySession,Integer>{ 
    
}
