package com.example.wellnessweb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.wellnessweb.models.Admin;

public interface AdminRepository extends JpaRepository<Admin,Integer> {

}
