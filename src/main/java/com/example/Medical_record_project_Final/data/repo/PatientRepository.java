package com.example.Medical_record_project_Final.data.repo;

import com.example.Medical_record_project_Final.data.entity.Doctor;
import com.example.Medical_record_project_Final.data.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {

    Optional<Patient> findByEgn(String egn);

    boolean existsByEgn(String egn);

    List<Patient> findAllByPersonalDoctor(Doctor personalDoctor);

    long countByPersonalDoctor(Doctor personalDoctor);
}