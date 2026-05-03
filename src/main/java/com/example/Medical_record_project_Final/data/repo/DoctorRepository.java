package com.example.Medical_record_project_Final.data.repo;

import com.example.Medical_record_project_Final.data.entity.Doctor;
import com.example.Medical_record_project_Final.data.entity.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {

    Optional<Doctor> findByDoctorIdentifier(String doctorIdentifier);

    boolean existsByDoctorIdentifier(String doctorIdentifier);

    List<Doctor> findAllByCanBePersonalDoctorTrue();

    List<Doctor> findAllBySpecialty(Specialty specialty);
}