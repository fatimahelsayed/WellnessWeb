package com.example.wellnessweb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.wellnessweb.models.TherapySession;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


public interface TherapySessionRepository extends JpaRepository<TherapySession,Integer>{ 
    List<TherapySession> findByTherapistIDAndStatusAndDateAfterAndStartTimeAfterOrderByDateAscStartTimeAsc(int therapistID, String status, LocalDate date, LocalTime startTime);
    TherapySession findFirstByTherapistIDAndStatusAndDateAfterAndStartTimeAfterOrderByDateAscStartTimeAsc(int therapistID, String status, LocalDate date, LocalTime startTime);
    List<TherapySession> findByTherapistID(int therapistID);
    TherapySession findById( int iD);
}
