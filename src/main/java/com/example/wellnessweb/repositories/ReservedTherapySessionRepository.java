package com.example.wellnessweb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.wellnessweb.models.ReservedTherapySession;

public interface ReservedTherapySessionRepository extends JpaRepository<ReservedTherapySession,Integer>{
    
}
