package com.example.Medical_record_project_Final.data.repo;

import com.example.Medical_record_project_Final.data.entity.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpecialtyRepository extends JpaRepository<Specialty, Integer> {

    Optional<Specialty> findByName(String name);

    boolean existsByName(String name);
}