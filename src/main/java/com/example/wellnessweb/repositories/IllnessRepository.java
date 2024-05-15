package com.example.wellnessweb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.wellnessweb.models.Illness;

import java.util.List;

public interface IllnessRepository extends JpaRepository<Illness, Integer> {
    List<Illness> findAll();
    Illness findByName(String illnessName);

}
