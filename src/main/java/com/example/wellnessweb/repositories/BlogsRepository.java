package com.example.wellnessweb.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.wellnessweb.models.Blogs;

public interface BlogsRepository extends JpaRepository<Blogs,Integer> {
    List<Blogs> findAll();
    List<Blogs> findAllByUserID(int userID);
    List<Blogs> findTop50ByOrderByDateDescTimeDesc();
    Optional<Blogs> findById(int ID);


}
