package com.example.wellnessweb.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.wellnessweb.models.TherapistRequest;

public interface TherapistRequestRepository extends JpaRepository<TherapistRequest,Integer> {
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    List<TherapistRequest> findTop5ByOrderByCreatedAtDesc();
    TherapistRequest findById(int id);
    TherapistRequest findByEmail(String email);
    List<TherapistRequest> findByIsAccepted(String isAccepted);
    List<TherapistRequest> findByIsAcceptedOrderByCreatedAtDesc(String isAccepted);
}
