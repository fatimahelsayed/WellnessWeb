package com.example.wellnessweb.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.wellnessweb.models.Therapist;

public interface TherapistRepository extends JpaRepository<Therapist,Integer>{
     List<Therapist> findTop5ByOrderByCreatedAtDesc();
     boolean existsByEmail(String email);
     Therapist findByEmail(String email);
}
