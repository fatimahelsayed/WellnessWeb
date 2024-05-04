package com.example.wellnessweb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.wellnessweb.models.TherapySession;
import java.util.List;


public interface TherapySessionRepository extends JpaRepository<TherapySession,Integer>{ 
    List<TherapySession> findByTherapistID(int therapistID);
}
