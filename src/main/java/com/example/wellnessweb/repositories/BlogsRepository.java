package com.example.wellnessweb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.wellnessweb.models.Blogs;

public interface BlogsRepository extends JpaRepository<Blogs,Integer> {
    
}
