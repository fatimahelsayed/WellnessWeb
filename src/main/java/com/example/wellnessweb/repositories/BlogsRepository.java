package com.example.wellnessweb.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.wellnessweb.models.Blogs;

public interface BlogsRepository extends JpaRepository<Blogs,Integer> {
    List<Blogs> findAll();
}
