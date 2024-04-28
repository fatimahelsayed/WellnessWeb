package com.example.wellnessweb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.wellnessweb.models.TherapistRequest;

public interface TherapistRequestRepository extends JpaRepository<TherapistRequest,Integer> {
    
}
