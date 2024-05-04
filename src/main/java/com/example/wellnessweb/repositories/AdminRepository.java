package com.example.wellnessweb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.wellnessweb.models.Admin;

public interface AdminRepository extends JpaRepository<Admin,Integer> {
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    Admin findByEmail(String email);
}
