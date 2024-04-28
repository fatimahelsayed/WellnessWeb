package com.example.wellnessweb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.wellnessweb.models.Therapist;

public interface TherapistRepository extends JpaRepository<Therapist,Integer>{
    
}
