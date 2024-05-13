package com.example.wellnessweb.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.wellnessweb.models.ReservedTherapySession;

public interface ReservedTherapySessionRepository extends JpaRepository<ReservedTherapySession,Integer>{
    boolean existsByTherapySessionID(int therapySessionID);
    ReservedTherapySession findByTherapySessionID(int therapySessionID);
    ReservedTherapySession findByCustomerID(int customerID);
    ReservedTherapySession findFirstByCustomerIDOrderByIDDesc(int customerID);
}
