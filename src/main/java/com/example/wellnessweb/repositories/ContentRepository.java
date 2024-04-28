package com.example.wellnessweb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.wellnessweb.models.Content;

public interface ContentRepository extends JpaRepository<Content,Integer> {
    
}
