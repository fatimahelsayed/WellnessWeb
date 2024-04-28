package com.example.wellnessweb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.wellnessweb.models.Customer;

public interface CustomerRepository extends JpaRepository<Customer,Integer>{ 
    
}
