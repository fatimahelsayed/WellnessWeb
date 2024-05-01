package com.example.wellnessweb.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.wellnessweb.models.Customer;

public interface CustomerRepository extends JpaRepository<Customer,Integer>{ 
    Customer findByUsername(String Username);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByUsername(String username);
    List<Customer> findTop5ByOrderByCreatedAtDesc();
}
