package com.example.wellnessweb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.wellnessweb.models.Illness;

public interface IllnessRepository extends JpaRepository<Illness,Integer>{ 
    
}